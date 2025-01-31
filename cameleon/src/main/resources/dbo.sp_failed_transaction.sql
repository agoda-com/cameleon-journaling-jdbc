IF EXISTS (SELECT 1 FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.sp_failed_transaction') AND type = N'P')
BEGIN
DROP PROCEDURE dbo.sp_failed_transaction;
END

CREATE PROCEDURE sp_failed_transaction
    @Id INT,
	@Name VARCHAR
AS
BEGIN

INSERT INTO dbo.example (id, name)
VALUES (@Id,@Name);
IF @Id =88
BEGIN
EXEC sys.sp_addmessage
    @msgnum = 60000,
    @severity = 16,
    @msgtext = N'This is a test message with one numeric parameter (%d), one string parameter (%s), and another string parameter (%s).',
    @lang = 'us_english';

DECLARE @msg NVARCHAR(2048) = FORMATMESSAGE(60000, 500, N'First string', N'second string');

THROW 60000, @msg, 1;

END
END