# Find users selling the book "Java Servlets"

SELECT first, last, book_condition, price
FROM Users, UserListings
WHERE user_id = Users.id
	AND title = 'Java Servlets';