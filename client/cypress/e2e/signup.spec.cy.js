describe('Sign up new users', () => {
    it('A user can sign up', () => {
        // Given the signup page
        cy.visit('http://127.0.0.1:8000/signup')

        // when we fill out the form for signup with username and password
        cy.get('input[name="username"]').type('testuser')
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then user should be logged in
        cy.get('.user-logged-in').should('exist');
    })
    it('A user cannot loging with same name twice', () => {
        // Given the signup page
        cy.visit('http://127.0.0.1:8000/signup')

        // when we fill out the sign up form with a username that already exists
        cy.get('input[name="username"]').type('testuser')
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then a generic error should be shown
        cy.get('.error').should('exist');

    })
    // TODO: give an specific error when the username already exists
    // TODO: enforce password policy
    // TODO: include security checks like CSRF token and captcha
})