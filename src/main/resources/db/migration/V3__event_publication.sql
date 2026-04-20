CREATE TABLE event_publication
(
    id                     BINARY(16)   NOT NULL,
    event_type             VARCHAR(512) NOT NULL,
    listener_id            VARCHAR(512) NOT NULL,
    publication_date       TIMESTAMP(6) NOT NULL,
    completion_date        TIMESTAMP(6) NULL,
    serialized_event       VARCHAR(4000) NOT NULL,
    status                 VARCHAR(20)  NOT NULL,
    completion_attempts    INT          NOT NULL,
    last_resubmission_date TIMESTAMP(6) NULL,
    PRIMARY KEY (id)
);