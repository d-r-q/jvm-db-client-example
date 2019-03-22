-- Function increments the input value by 1
CREATE OR REPLACE FUNCTION increment(i INT) RETURNS INT AS $$
BEGIN
  RETURN i + 1;
END;
$$ LANGUAGE plpgsql;