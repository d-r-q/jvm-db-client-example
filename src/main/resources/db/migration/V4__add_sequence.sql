CREATE SEQUENCE test_id_sq START 21;

ALTER TABLE test_table ALTER test_id SET DEFAULT NEXTVAL('test_id_sq');
ALTER TABLE test_table ADD COLUMN data VARCHAR(256);

INSERT INTO test_table (data) VALUES ('Демо сиквенсов');
