CREATE SEQUENCE group_sqs START 1;

CREATE TABLE groups
(
  id        bigint PRIMARY KEY DEFAULT NEXTVAL('group_sqs'),
  number varchar(256),
  students_count int
);

INSERT INTO groups (id, number)
VALUES (0, 'Default');

ALTER TABLE "students"
  ADD COLUMN student_group bigint NOT NULL REFERENCES groups(id) DEFAULT 0;

ALTER TABLE "students" ALTER COLUMN "student_group" DROP DEFAULT;
