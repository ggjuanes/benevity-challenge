describe('Sign up new users', () => {
    it('A user can sign up', () => {
        // Given the signup page
        cy.visit('http://127.0.0.1:8000/signup')

        // when we fill out the form for signup with username and password
        cy.get('input[name="username"]').type('testuser' + Math.random() * 1000)
        cy.get('input[name="password"]').type('testpassword')
        cy.get('button[type="submit"]').click()

        // then user should be logged in
        cy.get('.user-logged-in').should('exist');
    })
    // TODO: add test when signup cannot be done because of incorrect credentials

    // TODO: give an specific error when the username already exists
    // TODO: enforce password policy
    // TODO: include security checks like CSRF token and captcha
})