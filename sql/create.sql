use dmaa0218_1067477; -- Database name for dmaa0218_2Sem_3 Semesterprojekt

--================= Static content =================--
-- Type --
create table ItemType (
    id              int            primary key,
    name            nvarchar(255)  not null
);
insert into ItemType values
    (1, 'Laptop'),
	(2, 'Desktop'),
	(3, 'Printer');
go

-- ItemStatus --
create table ItemStatus (
    id              int            primary key,
    name            nvarchar(255)  not null
);
insert into ItemStatus values
    (1, 'OK'),
	(2, 'Decomissioned'),
	(3, 'Lost');
go
--================= Static content =================--

create table Employee (
    id              int            primary key identity(1,1),
	manr            varchar(9)     unique not null, --8 chars + null-terminator, varchar because no special characters
    [name]          nvarchar(255)  not null,
    unit            nvarchar(255)  not null,
    notes           nvarchar(255)  not null
);

create table [User] (
    employee_id     int            primary key references Employee(id) on delete cascade, -- Slightly crap name
    [password]      nvarchar(255)  not null
);

create table Item (
    id              int            primary key identity(1,1),
	serial          nvarchar(255)  unique not null, -- Items are uniquely identified by barcode scanners by their serial (barcode).
    [description]   nvarchar(255)  not null default '',
    model           nvarchar(255)  not null,
	itemType_id     int            not null foreign key references ItemType(id),
	itemStatus_id   int            not null foreign key references ItemStatus(id),
	notes           nvarchar(255)  not null default ''
);

create table UtilityItem (
    id              int            primary key identity(1,1),
	[name]          nvarchar(255)  not null,
    [description]   nvarchar(255)  not null default '',
	minStock        int            not null,
	currentStock    int            not null,
	notes           nvarchar(255)  not null default ''
);

create table Loan (
    id              int            primary key identity(1,1),
	[user_id]       int            not null foreign key references [User](employee_id),
	employee_id     int            not null foreign key references Employee(id),
	dateStart       date           not null,
	dateEnd         date           not null,
	notes           nvarchar(255)  not null default ''
);

create table LoanItemLine (
    id              int            primary key identity(1,1),
	loan_id         int            not null foreign key references Loan(id) on delete cascade,
	item_id         int            not null foreign key references Item(id),
	datePickedUp    datetime       null,
	dateReturned    datetime       null
);

create table LoanUtilityItemLine (
    id              int            primary key identity(1,1),
	loan_id         int            not null foreign key references Loan(id) on delete cascade,
	utilityItem_id  int            not null foreign key references UtilityItem(id),
	datePickedUp    datetime       null,
	dateReturned    datetime       null,
	lost            bit            not null default 0 -- MSSQL does not have booleans.
);
GO

CREATE VIEW vwUser
AS
	SELECT		E.*,
				U.[password]
	FROM		[User] U
	INNER JOIN	[Employee] E ON U.employee_id = E.id
GO

CREATE FUNCTION DoesDatesOverlap
(
	-- Add the parameters for the function here
	@span1DateStart date,
	@span1DateEnd date,
	@span2DateStart date,
	@span2DateEnd date
)
RETURNS BIT
AS
BEGIN
	-- Declare the return variable here
	DECLARE @result bit = 0

	-- Add the T-SQL statements to compute the return value here
	IF (@span1DateStart < @span2DateEnd AND @span1DateEnd > @span2DateStart)
		SET @result = 1

	-- Return the result of the function
	RETURN @result
END
GO

CREATE FUNCTION [IsItemAvailable]
(
	@itemId int,
	@dateStart date,
	@dateEnd date
)
RETURNS bit
AS
BEGIN
	DECLARE @Result bit = 1

	IF EXISTS(
	select		top 1 I.id
	from		Item I
	left join	LoanItemLine LIL on I.id = LIL.item_id
	left join	Loan L on LIL.loan_id = L.id
	where		I.id = @itemId
				--Check if @dateStart is between loans or lines startdate and enddate
	and			(dbo.DoesDatesOverlap(	@dateStart,
										@dateEnd,
										ISNULL(LIL.datePickedUp, L.dateStart),
										ISNULL(LIL.dateReturned, L.dateEnd)
									 ) = 1
				--OR itemstatus is not 'OK'
	or			I.itemStatus_id > 1))
		SET @Result = 0

	-- Return the result of the function
	RETURN @Result
END
GO

CREATE PROCEDURE GetItemsWithAvailable 
	-- Add the parameters for the stored procedure here
	@dateStart date,
	@dateEnd date
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select *, dbo.IsItemAvailable(id, @dateStart, @dateEnd) as available from Item
END
GO

CREATE FUNCTION [UtilityItemAvailableCount]
(
	@utilityItemId int,
	@dateStart date,
	@dateEnd date
)
RETURNS int
AS
BEGIN
	DECLARE @Result int = 0

	select @Result = (ui.currentStock - SUM(CONVERT(int, dbo.DoesDatesOverlap(@dateStart, @dateEnd, ISNULL(LUIL.datePickedUp, l.dateStart), ISNULL(LUIL.datePickedUp, l.dateEnd)))))
	from UtilityItem UI
	left join LoanUtilityItemLine LUIL on UI.id = LUIL.utilityItem_id
	left join Loan L on LUIL.loan_id = L.id and LUIL.lost = 0
	where UI.id = @utilityItemId
	group by UI.id, ui.currentStock

	-- Return the result of the function
	RETURN @Result
END
GO

CREATE PROCEDURE GetUtilityItemsWithAvailable
	@dateStart date,
	@dateEnd date
AS
BEGIN
	SET NOCOUNT ON;

	select	id,
			[name],
			[description],
			minStock,
			currentStock,
			notes,
			dbo.UtilityItemAvailableCount(id, @dateStart, @dateEnd) as available
	from	UtilityItem
END
GO

CREATE PROCEDURE InsertLoanItemLine
	-- Add the parameters for the stored procedure here
	@LoanId int,
	@ItemId int,
	@LineId int output
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SET @LineId = -1

	DECLARE @LoanDateStart DATE
	DECLARE @LoanDateEnd DATE

	SELECT @LoanDateStart = dateStart, @LoanDateEnd = dateEnd FROM [Loan] WHERE [id] = @LoanId

	IF (dbo.IsItemAvailable(@ItemId, @LoanDateStart, @LoanDateEnd) = 1)
	BEGIN
		insert into LoanItemLine(loan_id,item_id) values(@LoanId, @ItemId)
		SET @LineId = SCOPE_IDENTITY()
	END
END
GO

CREATE PROCEDURE InsertLoanUtilityItemLine
	@LoanId int,
	@UtilityItemId int,
	@LineId int output
AS
BEGIN
	SET NOCOUNT ON;

	SET @LineId = -1

	DECLARE @LoanDateStart DATE
	DECLARE @LoanDateEnd DATE

	SELECT @LoanDateStart = dateStart, @LoanDateEnd = dateEnd FROM [Loan] WHERE [id] = @LoanId

	IF (dbo.UtilityItemAvailableCount(@UtilityItemId, @LoanDateStart, @LoanDateEnd) > 0)
	BEGIN
		insert into LoanUtilityItemLine(loan_id, utilityItem_id) values(@LoanId, @UtilityItemId)
		SET @LineId = SCOPE_IDENTITY()
	END
END
GO