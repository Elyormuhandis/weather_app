CREATE FUNCTION notify_weather_updated()
    RETURNS TRIGGER
AS
$$
BEGIN
    PERFORM pg_notify('WEATHER_UPDATED', row_to_json(OLD)::text);
    RETURN NULL;
END;
$$
    LANGUAGE plpgsql;

CREATE TRIGGER weather_updated_trigger
    AFTER INSERT OR UPDATE
    ON weather
    FOR EACH ROW
EXECUTE PROCEDURE notify_weather_updated();
