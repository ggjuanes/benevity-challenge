describe('Login users', () => {
    it('A user with correct credentials can log in', () => {
        // Given a existing user
        let username = 'testuser' + Math.random() * 1000;
        cy.visit('http://127.0.0.1:8000/signup')

        // when we fill out the form for signup with username and password
        cy.get('input[name="username"]').type(username)
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()
        // Given the login pege
        cy.visit('http://127.0.0.1:8000/login')

        // when we fill out the form for login with username and password
        // TODO: use a fixture for this instead of relay on the signup
        cy.get('input[name="username"]').type(username)
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then user should be logged in
        cy.get('.user-logged-in').should('exist');
    })
    // TODO: add test when login cannot be done because of incorrect credentials
})