# car-rentals
building a java app to manage car rentals
https://grok.com/share/bGVnYWN5_e0538beb-e901-4da1-91a0-2010f42254fe

INSERT INTO users (username, password, name) VALUES ('testuser', 'testpass', 'Test User')
ON CONFLICT (username) DO NOTHING;  -- Avoid duplicates

INSERT INTO cars (name, model, year, description) VALUES 
('Toyota Camry', 'LE', 2023, 'A reliable sedan with good fuel efficiency.'),
('Ford Mustang', 'GT', 2024, 'High-performance sports car with powerful engine.');
