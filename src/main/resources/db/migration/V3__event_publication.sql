CREATE TABLE event_publication (
id BINARY(16) NOT NULL,
completion_attempts INT NOT NULL,
completion_date DATETIME(6),
event_type VARCHAR(512) NOT NULL,
last_resubmission_date DATETIME(6),
listener_id VARCHAR(512) NOT NULL,
publication_date DATETIME(6) NOT NULL,
serialized_event VARCHAR(4000) NOT NULL,
status VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX event_publication_by_completion_date_idx
    ON event_publication (completion_date);
