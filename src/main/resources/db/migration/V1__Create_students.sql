CREATE SEQUENCE students_sqs;

CREATE TABLE "students"
(
  id        bigint PRIMARY KEY DEFAULT NEXTVAL('students_sqs'),
  full_name varchar(256)
)
