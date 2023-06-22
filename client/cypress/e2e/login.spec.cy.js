describe('Login users', () => {
    it('A user with correct credentials can log in', () => {
        // Given the login paege
        cy.visit('http://127.0.0.1:8000/login')

        // when we fill out the form for login with username and password
        cy.get('input[name="username"]').type('testuser')
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then user should be logged in
        cy.get('.user-logged-in').should('exist');
    })
    it('A user with incorrect credentials cannot log in', () => {
        // Given the login page
        cy.visit('http://127.0.0.1:8000/login')

        // when we fill out the log in form with a username that already exists
        cy.get('input[name="username"]').type('testuser')
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then a generic error should be shown
        cy.get('.error').should('exist');

    })
})