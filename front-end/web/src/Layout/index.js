/**
* Layout para header+Sidebar con un componente en su interior
*/
import React from 'react';
import PersistentDrawerLeft from '../Sidebar';

const Layout = ({ children }) => {
  return (
    <PersistentDrawerLeft>
      {children}
    </PersistentDrawerLeft>
  );
};

export default Layout;