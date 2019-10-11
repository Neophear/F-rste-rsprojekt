use dmaa0218_1067477; -- Database name for dmaa0218_2Sem_3 Semesterprojekt

begin transaction

drop view dbo.vwUser;
drop table LoanItemLine;
drop table LoanUtilityItemLine;
drop table Loan;
drop table Item
drop table UtilityItem
drop table ItemType;
drop table ItemStatus;
drop table [User]
drop table Employee;

drop procedure [dbo].[GetItemsWithAvailable]
drop procedure dbo.GetUtilityItemsWithAvailable
drop procedure dbo.InsertLoanItemLine
drop procedure dbo.InsertLoanUtilityItemLine

drop function dbo.DoesDatesOverlap
drop function dbo.IsItemAvailable
drop function dbo.UtilityItemAvailableCount

commit