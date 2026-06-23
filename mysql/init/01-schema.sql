-- 企业研发全流程智能体平台 - 数据库初始化脚本
-- MySQL 8.x

CREATE DATABASE IF NOT EXISTS agent_platform DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE agent_platform;

-- ============================================================
-- 会话与消息
-- ============================================================

CREATE TABLE IF NOT EXISTS agent_session (
    session_id     VARCHAR(64) PRIMARY KEY,
    user_id        VARCHAR(64) NOT NULL,
    title          VARCHAR(256),
    agent_type     VARCHAR(32) NOT NULL,
    status         VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    pinned         BOOLEAN NOT NULL DEFAULT FALSE,
    context        JSON,
    last_message_at DATETIME,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_status (status)
);

CREATE TABLE IF NOT EXISTS agent_message (
    message_id     VARCHAR(64) PRIMARY KEY,
    session_id     VARCHAR(64) NOT NULL,
    role           VARCHAR(16) NOT NULL,
    content        TEXT,
    tool_calls     JSON,
    token_count    INT,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_session (session_id)
);

CREATE TABLE IF NOT EXISTS task_plan (
    plan_id        VARCHAR(64) PRIMARY KEY,
    session_id     VARCHAR(64) NOT NULL,
    status         VARCHAR(16) NOT NULL DEFAULT 'PLANNED',
    plan_dag       JSON,
    result         JSON,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_session (session_id)
);

-- ============================================================
-- Skill 技能体系
-- ============================================================

CREATE TABLE IF NOT EXISTS skill_definition (
    skill_id       VARCHAR(64) PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    description    TEXT NOT NULL,
    version        VARCHAR(32) NOT NULL,
    category       VARCHAR(32) NOT NULL,
    input_schema   JSON NOT NULL,
    output_schema  JSON NOT NULL,
    execution_mode VARCHAR(16) NOT NULL DEFAULT 'SYNC',
    endpoint       VARCHAR(512) NOT NULL,
    auth_type      VARCHAR(32),
    config         JSON,
    status         VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    created_by     VARCHAR(64),
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_status (status)
);

CREATE TABLE IF NOT EXISTS skill_call_log (
    log_id         VARCHAR(64) PRIMARY KEY,
    session_id     VARCHAR(64) NOT NULL,
    plan_id        VARCHAR(64),
    skill_id       VARCHAR(64) NOT NULL,
    input_params   JSON NOT NULL,
    output_result  JSON,
    status         VARCHAR(16) NOT NULL,
    duration_ms    INT,
    token_usage    JSON,
    error_message  TEXT,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_session (session_id),
    INDEX idx_skill (skill_id),
    INDEX idx_time (created_at)
);

-- ============================================================
-- 知识库
-- ============================================================

CREATE TABLE IF NOT EXISTS knowledge_base (
    kb_id          VARCHAR(64) PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    description    TEXT,
    embedding_model VARCHAR(64) NOT NULL,
    chunk_strategy VARCHAR(32) NOT NULL DEFAULT 'SEMANTIC',
    milvus_collection VARCHAR(128) NOT NULL,
    doc_count      INT NOT NULL DEFAULT 0,
    chunk_count    INT NOT NULL DEFAULT 0,
    status         VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    created_by     VARCHAR(64),
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS kb_document (
    doc_id         VARCHAR(64) PRIMARY KEY,
    kb_id          VARCHAR(64) NOT NULL,
    title          VARCHAR(256) NOT NULL,
    source         VARCHAR(512) NOT NULL,
    doc_type       VARCHAR(32) NOT NULL,
    format         VARCHAR(16) NOT NULL,
    file_path      VARCHAR(512),
    chunk_count    INT NOT NULL DEFAULT 0,
    status         VARCHAR(16) NOT NULL DEFAULT 'PROCESSING',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_kb (kb_id)
);

-- ============================================================
-- 代码评审
-- ============================================================

CREATE TABLE IF NOT EXISTS review_task (
    review_id      VARCHAR(64) PRIMARY KEY,
    session_id     VARCHAR(64),
    mr_id          VARCHAR(64),
    mr_title       VARCHAR(256),
    project_id     VARCHAR(64) NOT NULL,
    reviewer_type  VARCHAR(16) NOT NULL DEFAULT 'AI',
    status         VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    summary        JSON,
    created_by     VARCHAR(64),
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at   DATETIME,
    INDEX idx_project (project_id),
    INDEX idx_mr (mr_id)
);

CREATE TABLE IF NOT EXISTS review_issue (
    issue_id       VARCHAR(64) PRIMARY KEY,
    review_id      VARCHAR(64) NOT NULL,
    severity       VARCHAR(16) NOT NULL,
    category       VARCHAR(32) NOT NULL,
    file_path      VARCHAR(512) NOT NULL,
    start_line     INT NOT NULL,
    end_line       INT,
    code_snippet   TEXT,
    description    TEXT NOT NULL,
    suggestion     TEXT,
    fixed_code     TEXT,
    status         VARCHAR(16) NOT NULL DEFAULT 'OPEN',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_review (review_id),
    INDEX idx_severity (severity)
);

-- ============================================================
-- 故障排障
-- ============================================================

CREATE TABLE IF NOT EXISTS diagnosis_task (
    diagnosis_id   VARCHAR(64) PRIMARY KEY,
    session_id     VARCHAR(64),
    service_name   VARCHAR(128),
    log_content    TEXT NOT NULL,
    status         VARCHAR(16) NOT NULL DEFAULT 'RUNNING',
    result         JSON,
    created_by     VARCHAR(64),
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at   DATETIME,
    INDEX idx_service (service_name)
);

-- ============================================================
-- 用户与权限
-- ============================================================

CREATE TABLE IF NOT EXISTS sys_user (
    user_id        VARCHAR(64) PRIMARY KEY,
    username       VARCHAR(64) NOT NULL UNIQUE,
    password_hash  VARCHAR(256) NOT NULL,
    name           VARCHAR(128) NOT NULL,
    email          VARCHAR(128),
    role           VARCHAR(32) NOT NULL DEFAULT 'DEVELOPER',
    department     VARCHAR(128),
    status         VARCHAR(16) NOT NULL DEFAULT 'ENABLED',
    last_login_at  DATETIME,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_role (role),
    INDEX idx_department (department)
);

CREATE TABLE IF NOT EXISTS sys_permission (
    permission_id  VARCHAR(64) PRIMARY KEY,
    resource       VARCHAR(32) NOT NULL,
    action         VARCHAR(16) NOT NULL,
    description    VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS sys_role_permission (
    role           VARCHAR(32) NOT NULL,
    permission_id  VARCHAR(64) NOT NULL,
    PRIMARY KEY (role, permission_id),
    FOREIGN KEY (permission_id) REFERENCES sys_permission(permission_id)
);

-- ============================================================
-- 模型配置
-- ============================================================

CREATE TABLE IF NOT EXISTS model_config (
    model_id         VARCHAR(64) PRIMARY KEY,
    name             VARCHAR(128) NOT NULL,
    deploy_type      VARCHAR(16) NOT NULL,
    api_endpoint     VARCHAR(512) NOT NULL,
    api_key_encrypted VARCHAR(256),
    max_context_length INT NOT NULL DEFAULT 8192,
    default_temperature DECIMAL(3,2) NOT NULL DEFAULT 0.70,
    status           VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    is_default       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 通知
-- ============================================================

CREATE TABLE IF NOT EXISTS notification_channel (
    channel_type     VARCHAR(32) PRIMARY KEY,
    enabled          BOOLEAN NOT NULL DEFAULT FALSE,
    config           JSON NOT NULL,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification_rule (
    rule_id          VARCHAR(64) PRIMARY KEY,
    event_type       VARCHAR(32) NOT NULL,
    channels         JSON NOT NULL,
    recipients       JSON NOT NULL,
    enabled          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_event_type (event_type)
);

CREATE TABLE IF NOT EXISTS notification (
    notification_id  VARCHAR(64) PRIMARY KEY,
    user_id          VARCHAR(64) NOT NULL,
    type             VARCHAR(32) NOT NULL,
    title            VARCHAR(256) NOT NULL,
    summary          VARCHAR(512),
    resource_id      VARCHAR(64),
    read             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_unread (user_id, read),
    INDEX idx_user_time (user_id, created_at)
);

-- ============================================================
-- 微调
-- ============================================================

CREATE TABLE IF NOT EXISTS finetune_task (
    task_id          VARCHAR(64) PRIMARY KEY,
    name             VARCHAR(128) NOT NULL,
    base_model       VARCHAR(128) NOT NULL,
    dataset_id       VARCHAR(64) NOT NULL,
    status           VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    lora_config      JSON NOT NULL,
    training_config  JSON NOT NULL,
    training_progress JSON,
    training_result  JSON,
    error_message    TEXT,
    created_by       VARCHAR(64),
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at     DATETIME
);

CREATE TABLE IF NOT EXISTS finetune_dataset (
    dataset_id       VARCHAR(64) PRIMARY KEY,
    name             VARCHAR(128) NOT NULL,
    type             VARCHAR(32) NOT NULL,
    description      TEXT,
    sample_count     INT NOT NULL DEFAULT 0,
    status           VARCHAR(16) NOT NULL DEFAULT 'BUILDING',
    created_by       VARCHAR(64),
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS finetune_sample (
    sample_id        VARCHAR(64) PRIMARY KEY,
    dataset_id       VARCHAR(64) NOT NULL,
    instruction      TEXT NOT NULL,
    input            TEXT,
    output           TEXT NOT NULL,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 代码模板
-- ============================================================

CREATE TABLE IF NOT EXISTS code_template (
    template_id    VARCHAR(64) PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    category       VARCHAR(32) NOT NULL,
    language       VARCHAR(16) NOT NULL DEFAULT 'Java',
    framework      VARCHAR(32) NOT NULL DEFAULT 'SpringBoot',
    template_content TEXT NOT NULL,
    variables      JSON NOT NULL,
    status         VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 初始化数据：管理员用户
-- ============================================================

INSERT INTO sys_user (user_id, username, password_hash, name, role, status)
VALUES ('admin', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrqK3eD4R8Jm4Sj5kKQOGx8W2HTbPhe', '系统管理员', 'ADMIN', 'ENABLED')
ON DUPLICATE KEY UPDATE user_id = user_id;

-- 初始化权限数据
INSERT INTO sys_permission (permission_id, resource, action, description) VALUES
('perm_chat_read', 'chat', 'read', '查看对话'),
('perm_chat_write', 'chat', 'write', '发送对话'),
('perm_skill_read', 'skill', 'read', '查看技能'),
('perm_skill_write', 'skill', 'write', '管理技能'),
('perm_knowledge_read', 'knowledge', 'read', '查看知识库'),
('perm_knowledge_write', 'knowledge', 'write', '管理知识库'),
('perm_review_read', 'review', 'read', '查看评审'),
('perm_review_write', 'review', 'write', '发起评审'),
('perm_diagnosis_read', 'diagnosis', 'read', '查看排障'),
('perm_diagnosis_write', 'diagnosis', 'write', '发起排障'),
('perm_finetune_read', 'finetune', 'read', '查看微调'),
('perm_finetune_write', 'finetune', 'write', '管理微调'),
('perm_metrics_read', 'metrics', 'read', '查看度量'),
('perm_settings_read', 'settings', 'read', '查看设置'),
('perm_settings_write', 'settings', 'write', '修改设置'),
('perm_users_read', 'users', 'read', '查看用户'),
('perm_users_write', 'users', 'write', '管理用户')
ON DUPLICATE KEY UPDATE permission_id = permission_id;

-- ADMIN 角色拥有所有权限
INSERT INTO sys_role_permission (role, permission_id)
SELECT 'ADMIN', permission_id FROM sys_permission
ON DUPLICATE KEY UPDATE role = role;

-- DEVELOPER 角色权限
INSERT INTO sys_role_permission (role, permission_id) VALUES
('DEVELOPER', 'perm_chat_read'),
('DEVELOPER', 'perm_chat_write'),
('DEVELOPER', 'perm_skill_read'),
('DEVELOPER', 'perm_knowledge_read'),
('DEVELOPER', 'perm_review_read'),
('DEVELOPER', 'perm_review_write'),
('DEVELOPER', 'perm_diagnosis_read'),
('DEVELOPER', 'perm_finetune_read'),
('DEVELOPER', 'perm_metrics_read'),
('DEVELOPER', 'perm_settings_read')
ON DUPLICATE KEY UPDATE role = role;
