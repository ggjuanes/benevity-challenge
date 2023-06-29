import React from 'react';
import {render} from '@testing-library/react';
import Navbar from "./Navbar";

test('Nav should have home link', () => {
  const { getByText } = render(<Navbar />);
  let home = getByText('Home');

  expect(home).toBeInTheDocument();
});

test('Nav should have login link', () => {
  const { getByText } = render(<Navbar />);
  let login = getByText('Log in');

  expect(login).toBeInTheDocument();
});

test('Nav should have signup link', () => {
  const { getByText } = render(<Navbar />);
  let signup = getByText('Sign up');

  expect(signup).toBeInTheDocument();
});
