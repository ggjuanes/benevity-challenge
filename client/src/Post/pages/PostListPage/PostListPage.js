import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
// Import Components
import PostList from '../../components/PostList';
import PostCreateWidget from '../../components/PostCreateWidget';
// Import Actions
import { addPostRequest, deletePostRequest, fetchPosts } from '../../PostActions';

const PostListPage = ({ token }) => {
  const showAddPost = true;
  const dispatch = useDispatch();
  const posts = useSelector(state => state.posts.data);

  useEffect(() => {
    dispatch(fetchPosts(token));
  },[]);

  const handleDeletePost = post => {
    if (confirm('Do you want to delete this post')) { // eslint-disable-line
      dispatch(deletePostRequest(token, post));
    }
  };

  const handleAddPost = (post) => {
    dispatch(addPostRequest(post, token));
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-6">
          <PostCreateWidget addPost={handleAddPost} showAddPost={showAddPost} />
        </div>
        <div className="col-6">
          <PostList handleDeletePost={handleDeletePost} posts={posts} />
        </div>
      </div>
    </div>
  );
};

PostListPage.propTypes = {
  token: PropTypes.string.isRequired
};

PostListPage.defaultProps = {
  showAddPost: true
};


export default PostListPage;
