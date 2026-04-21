CREATE TABLE IF NOT EXISTS event_publication (
    id               BINARY(16) NOT NULL,
    completion_date  DATETIME(6),
    event_type       VARCHAR(512) NOT NULL,
    listener_id      VARCHAR(512) NOT NULL,
    publication_date DATETIME(6) NOT NULL,
    serialized_event TEXT NOT NULL,
    PRIMARY KEY (ID)
    ) ENGINE=InnoDB;


CREATE INDEX event_publication_by_completion_date_idx
    ON event_publication (completion_date);