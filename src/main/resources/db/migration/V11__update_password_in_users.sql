UPDATE users
SET password = CASE user_name
    WHEN 'jean' THEN '13385707f1ed6f2edfad23369525aca0ff40153716c510c9b6906fd78e17bdda2ee22ff0b9376a67'
    WHEN 'zion' THEN '91421d7a542a78a77cb6623ebb34e1641d7bfce92b9a8eafd905b9c5c8e004a823c8a8ce0178368e'
END
WHERE user_name IN ('jean', 'zion');