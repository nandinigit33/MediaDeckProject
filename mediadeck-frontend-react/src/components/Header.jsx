import React from 'react';
import { Navbar, Nav, NavDropdown, Container } from 'react-bootstrap';

const Header = ({ isAuthenticated, navMode }) => (
  <Navbar className={navMode} expand="lg" fixed="top">
    <Container>
      <Navbar.Brand href="/">
        MEDIADECK <span className="glyphicon glyphicon-film" />
      </Navbar.Brand>
      <Navbar.Toggle />
      <Navbar.Collapse>
        <Nav className="me-auto">
          <Nav.Link href="/">HOME</Nav.Link>
          {isAuthenticated && <Nav.Link href="/profile">MY PROFILE</Nav.Link>}

          {isAuthenticated && (
            <NavDropdown title={<span className="glyphicon glyphicon-cog" />} id="settings-menu">
              <NavDropdown.Item href="/settings/change-theme">CUSTOMIZE THEME</NavDropdown.Item>
              <NavDropdown.Item href="/settings/update-email">UPDATE EMAIL</NavDropdown.Item>
              <NavDropdown.Item href="/settings/change-password">CHANGE PASSWORD</NavDropdown.Item>
              <NavDropdown.Item href="/settings/delete-account">DELETE ACCOUNT</NavDropdown.Item>
            </NavDropdown>
          )}
        </Nav>

        <Nav className="ms-auto">
          <NavDropdown title={<span className="glyphicon glyphicon-search" />} id="search-menu">
            <NavDropdown.Item href="/search">SEARCH MOVIES</NavDropdown.Item>
            <NavDropdown.Item href="/user_search">SEARCH USERS</NavDropdown.Item>
          </NavDropdown>

          {!isAuthenticated ? (
            <Nav.Link href="/signin">SIGN IN</Nav.Link>
          ) : (
            <Nav.Link href="/signout">SIGN OUT</Nav.Link>
          )}
        </Nav>
      </Navbar.Collapse>
    </Container>
  </Navbar>
);

export default Header;