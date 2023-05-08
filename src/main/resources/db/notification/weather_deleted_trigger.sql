CREATE FUNCTION notify_weather_deleted()
    RETURNS TRIGGER
AS
$$
BEGIN
   PERFORM pg_notify('WEATHER_DELETED', row_to_json(OLD)::text);
   RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER weather_deleted_trigger
   AFTER DELETE
   ON weather
   FOR EACH ROW
    EXECUTE PROCEDURE notify_weather_deleted();
