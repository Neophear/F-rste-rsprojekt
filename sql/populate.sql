use dmaa0218_1067477; -- Database name for dmaa0218_2Sem_3 Semesterprojekt

insert into Employee(manr,[name],unit,notes) values 
    ('00123456','Admin','Backdoor','Built-in backdoor superuser');
insert into [User](employee_id,[password]) values
    (1, 'admin');

insert into Item(serial,[description],model,itemType_id,itemStatus_id,notes) values
    ('1','15.6" Workstation laptop. 8GB RAM, i5-2520M, 256GB SSD.', 'Lenovo ThinkPad W520', 1, 1, ''),
	('2', 'Alienware', 'M10', 2, 1, '');
insert into UtilityItem([name],[description],minStock,currentStock,notes) values
    ('Generic USB Mouse','Regular, ordinary USB-connected pointing device.', 15, 23, 'More likely to be lost than returned.'),
	('Network-cable, 5m','Network-cable RJ45 CAT-5E, 5 meters.', 40, 100, '');

insert into Loan([user_id],employee_id,dateStart,dateEnd,notes) values
    (1, 1, '2018-12-04', '2018-12-05', 'Test loan'),
	(1, 1, '2018-12-11', '2018-12-24', '');
insert into LoanItemLine(loan_id,item_id,datePickedUp,dateReturned) values
	(1, 1, '2018-12-04 08:05:24', null),
	(2, 2, null, null);
insert into LoanUtilityItemLine(loan_id,utilityItem_id,datePickedUp,dateReturned,lost) values
    (1, 1, '2018-12-04 08:05:26', null, 0),
	(2, 1, null, null, 0),
	(2, 1, null, null, 0),
	(2, 1, null, null, 1);