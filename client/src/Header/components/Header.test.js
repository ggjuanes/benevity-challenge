import React from 'react';
import {getByAltText, render} from '@testing-library/react';
import Header from "./Header";

test('Header should be render', () => {
  const { getByText, getByAltText } = render(<Header />);
  const header = getByText(/Alaya Blog/i);
  let logo = getByAltText('Logo');

  expect(header).toBeInTheDocument();
  expect(logo).toBeInTheDocument();
});
