import fetch from 'isomorphic-fetch';

export const API_URL = 'http://localhost:8888';
export const HTTP_METHODS = {
    GET: 'get',
    POST: 'post',
    DELETE: 'delete',
}

export default async (endpoint, method = 'get', body, token) => {
  return fetch(`${API_URL}/${endpoint}`, {
    headers: {
      'content-type': 'application/json',
      'Authorization': token !== undefined? `Bearer ${token}` : '',
    },
    method,
    body: JSON.stringify(body),
  })
  .then(response => {
    return response.json().then(json => {
      return ({json, response});
    }).catch(err => {
      console.error(`'${err}' happened, but no big deal!`);
        return ({json: {}, response})
    });
  })
  .then(({ json, response }) => {
    if (!response.ok) {
      return Promise.reject({
        status: response.status,
        json: json
      });
    }

    return json;
  })
  .then(
    response => response,
    error => error
  ).catch(console.error);
}
