"""
Agent Service - LangGraph Agent Engine

Implements the core Agent Loop using LangGraph StateGraph:
- Intent recognition node
- Task planning node with Send() parallel fan-out
- Skill execution node
- Result synthesis node
- Conditional routing
"""
from langgraph.graph import StateGraph, END
from langgraph.graph import Send
from langgraph.checkpoint.memory import MemorySaver
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, AIMessage, SystemMessage

from app.core.config import settings
from app.schemas.state import AgentState, SubTaskState


# Initialize LLM
llm = ChatOpenAI(
    model=settings.llm_model,
    api_key=settings.llm_api_key,
    base_url=settings.llm_api_base,
    temperature=settings.llm_temperature,
    max_tokens=settings.llm_max_tokens,
)


def intent_recognition(state: AgentState) -> dict:
    """意图识别节点"""
    messages = state["messages"]
    user_input = messages[-1].content if messages else ""

    # Mock intent recognition
    prompt = f"""你是意图识别模块。分析用户输入，识别意图并提取实体。

用户输入：{user_input}

请返回 JSON 格式：
{{
  "intent": "CODE_GEN|CODE_REVIEW|FAULT_DIAG|KNOWLEDGE_QA|GENERAL",
  "entities": {{...}},
  "needs_clarification": false
}}"""

    try:
        response = llm.invoke([SystemMessage(content=prompt)])
        # Parse mock response
        return {
            "intent": "CODE_GEN",
            "entities": {"language": "Java", "framework": "SpringBoot"},
            "needs_clarification": False,
        }
    except Exception:
        # Fallback mock
        return {
            "intent": "GENERAL",
            "entities": {},
            "needs_clarification": False,
        }


def task_planning(state: AgentState) -> dict:
    """任务规划节点 - 生成子任务列表"""
    intent = state.get("intent", "GENERAL")
    entities = state.get("entities", {})

    # Mock task planning based on intent
    plans = {
        "CODE_GEN": [
            {"task_id": "t1", "skill_name": "requirement_analysis", "params": entities},
            {"task_id": "t2", "skill_name": "code_generation", "params": entities},
            {"task_id": "t3", "skill_name": "code_review_auto", "params": entities},
        ],
        "CODE_REVIEW": [
            {"task_id": "t1", "skill_name": "diff_analysis", "params": entities},
            {"task_id": "t2", "skill_name": "code_review_auto", "params": entities},
        ],
        "FAULT_DIAG": [
            {"task_id": "t1", "skill_name": "log_analysis", "params": entities},
            {"task_id": "t2", "skill_name": "metric_query", "params": entities},
            {"task_id": "t3", "skill_name": "trace_analysis", "params": entities},
        ],
        "KNOWLEDGE_QA": [
            {"task_id": "t1", "skill_name": "knowledge_retrieval", "params": entities},
        ],
    }

    task_plan = plans.get(intent, [
        {"task_id": "t1", "skill_name": "general_chat", "params": entities}
    ])

    return {"task_plan": task_plan}


def should_continue(state: AgentState) -> str:
    """条件路由：判断是否需要继续迭代"""
    if state.get("needs_clarification"):
        return "clarify"
    if state.get("final_output"):
        return "synthesize"
    if state.get("iteration", 0) >= state.get("max_iterations", 5):
        return "synthesize"
    return "execute"


def route_to_subtasks(state: AgentState) -> list[Send]:
    """将任务规划结果路由为并行子任务（Send fan-out）"""
    task_plan = state.get("task_plan", [])
    return [
        Send("execute_skill", SubTaskState(
            task_id=task["task_id"],
            skill_name=task["skill_name"],
            params=task.get("params", {}),
            result=None,
        ))
        for task in task_plan
    ]


def execute_skill(state: SubTaskState) -> dict:
    """Skill 执行节点 - 执行单个子任务"""
    skill_name = state["skill_name"]
    params = state["params"]

    # Mock skill execution
    mock_results = {
        "requirement_analysis": {"analysis": "用户需求分析完成", "entities": params},
        "code_generation": {"code": "// Generated code placeholder", "language": "Java"},
        "code_review_auto": {"issues": [], "score": 95},
        "diff_analysis": {"changed_files": 3, "additions": 50, "deletions": 20},
        "log_analysis": {"error_patterns": ["NullPointerException"], "suggestion": "检查空值"},
        "metric_query": {"cpu_usage": "85%", "memory_usage": "70%"},
        "trace_analysis": {"slow_spans": [], "total_duration_ms": 1500},
        "knowledge_retrieval": {"documents": [{"title": "编码规范", "relevance": 0.95}]},
        "general_chat": {"response": "通用回复"},
    }

    result = mock_results.get(skill_name, {"result": "Mock skill execution result"})
    return {"result": result}


def result_synthesis(state: AgentState) -> dict:
    """结果汇聚节点"""
    tool_results = state.get("tool_results", [])
    intent = state.get("intent", "GENERAL")

    # Mock synthesis
    output = f"基于意图 {intent} 的处理结果已生成。共执行 {len(state.get('task_plan', []))} 个子任务。"

    return {
        "final_output": output,
        "iteration": state.get("iteration", 0) + 1,
    }


def clarify(state: AgentState) -> dict:
    """澄清节点 - 需要更多信息时调用"""
    return {
        "final_output": "请提供更多信息以便我更好地帮助您。",
        "needs_clarification": False,
    }


def build_agent_graph() -> StateGraph:
    """构建 Agent StateGraph"""
    graph = StateGraph(AgentState)

    # Add nodes
    graph.add_node("intent_recognition", intent_recognition)
    graph.add_node("task_planning", task_planning)
    graph.add_node("execute_skill", execute_skill)
    graph.add_node("result_synthesis", result_synthesis)
    graph.add_node("clarify", clarify)

    # Set entry point
    graph.set_entry_point("intent_recognition")

    # Add edges
    graph.add_edge("intent_recognition", "task_planning")

    # Conditional routing from task_planning: fan-out to parallel skill execution
    graph.add_conditional_edges(
        "task_planning",
        route_to_subtasks,
        {"execute_skill": "execute_skill"},
    )

    # All skill executions converge to result synthesis
    graph.add_edge("execute_skill", "result_synthesis")

    # Conditional routing from result synthesis
    graph.add_conditional_edges(
        "result_synthesis",
        should_continue,
        {
            "clarify": "clarify",
            "synthesize": END,
            "execute": "task_planning",  # Re-plan if needed
        },
    )

    graph.add_edge("clarify", END)

    return graph


# Build and compile the graph with checkpointer
checkpointer = MemorySaver()
agent_graph = build_agent_graph().compile(checkpointer=checkpointer)
