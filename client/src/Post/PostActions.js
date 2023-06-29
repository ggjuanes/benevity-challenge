import callApi, {HTTP_METHODS} from '../util/apiCaller';

// Export Constants
export const ADD_POST = 'ADD_POST';
export const ADD_POSTS = 'ADD_POSTS';
export const DELETE_POST = 'DELETE_POST';

// Export Actions
export function addPost(post) {
    return {
        type: ADD_POST,
        post,
    };
}

export function addPostRequest(post, token) {
    return (dispatch) => {
        return callApi('posts', 'post', {
            name: post.name,
            title: post.title,
            content: post.content,
        }, token).then(() => dispatch(addPost(post)));
    };
}

export function addPosts(posts) {
    return {
        type: ADD_POSTS,
        posts,
    };
}

export function fetchPosts(token) {
    return (dispatch) => {
        return callApi('posts', HTTP_METHODS.GET, undefined, token).then(res => {
            dispatch(addPosts(res));
        });
    };
}

export function fetchPost(token, title) {
    return (dispatch) => {
        return callApi(`posts/${title}`, HTTP_METHODS.GET, undefined, token).then(res => dispatch(addPost(res.post)));
    };
}

export function deletePost(title) {
    return {
        type: DELETE_POST,
        title,
    };
}

export function deletePostRequest(token, title) {
    return (dispatch) => {
        return callApi(`posts/${title}`, 'delete', undefined, token)
            .catch((err) => {
              console.log(err);
            })
            .then((response) => {
            console.log(response);
            return dispatch(deletePost(title));
        });
    };
}
