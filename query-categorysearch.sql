# Find books in the "Computer Science" category

SELECT isbn, title, author, book_condition, price
FROM UserListings
WHERE category = 'Computer Science'
ORDER BY price;