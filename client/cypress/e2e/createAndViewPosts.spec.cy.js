describe('Login users', () => {
    it('A user with correct credentials can log in', () => {
        // Given a existing user
        const username = 'testuser' + Math.random() * 1000;
        createUser(username);
        logIn(username);
        // Given the login pege
        cy.visit('http://127.0.0.1:8000/home')

        const name = 'name' + Math.random() * 1000;
        const title = 'title' + Math.random() * 1000;
        const content = 'content' + Math.random() * 1000;
        cy.get('input[name="name"]').type(name)
        cy.get('input[name="title"]').type(title)
        cy.get('textarea[name="content"]').type(content)
        cy.get('.createPost button[type="button"]').click()


        // then user should be logged in
        cy.get('h2 a').contains(title);
    })
    // TODO: add test when login cannot be done because of incorrect credentials
})

function createUser(username) {
    cy.visit('http://127.0.0.1:8000/signup')

    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type('testpassword')
    cy.get('button[type="submit"]').click()
}

function logIn(username) {
    cy.visit('http://127.0.0.1:8000/login')

    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type('testpassword')
    cy.get('button[type="submit"]').click()
}