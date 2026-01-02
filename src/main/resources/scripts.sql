-- Make sure schema exists
CREATE SCHEMA IF NOT EXISTS eventflowdb;

-- Users table
CREATE TABLE eventflowdb.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, OPERATOR, VIEWER
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON eventflowdb.users(username);

-- Incidents table
CREATE TABLE eventflowdb.incidents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(30) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_by BIGINT REFERENCES eventflowdb.users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_incidents_status ON eventflowdb.incidents(status);
CREATE INDEX idx_incidents_severity ON eventflowdb.incidents(severity);
CREATE INDEX idx_incidents_created_at ON eventflowdb.incidents(created_at);

-- Incident Assignments
CREATE TABLE eventflowdb.incident_assignments (
    id BIGSERIAL PRIMARY KEY,
    incident_id BIGINT REFERENCES eventflowdb.incidents(id),
    assigned_to BIGINT REFERENCES eventflowdb.users(id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Incident Comments
CREATE TABLE eventflowdb.incident_comments (
    id BIGSERIAL PRIMARY KEY,
    incident_id BIGINT REFERENCES eventflowdb.incidents(id),
    commented_by BIGINT REFERENCES eventflowdb.users(id),
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Audit Logs
CREATE TABLE eventflowdb.audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID,
    event_type VARCHAR(50),
    entity_id BIGINT,
    entity_type VARCHAR(50),
    performed_by BIGINT,
    payload JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_event_type ON eventflowdb.audit_logs(event_type);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE incident_severity_stats (
    severity VARCHAR(20) PRIMARY KEY,
    count BIGINT
);

CREATE TABLE incident_type_stats (
    type VARCHAR(30) PRIMARY KEY,
    count BIGINT
);

CREATE TABLE daily_incident_stats (
    date DATE PRIMARY KEY,
    total BIGINT
);