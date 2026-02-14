import { Navbar as BsNavbar, Nav, Container } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom';

export default function Navbar() {
  const location = useLocation();

  return (
    <BsNavbar bg="dark" variant="dark" expand="lg" className="mb-4">
      <Container>
        <BsNavbar.Brand as={Link} to="/">Controle de Estoque</BsNavbar.Brand>
        <BsNavbar.Toggle />
        <BsNavbar.Collapse>
          <Nav>
            <Nav.Link as={Link} to="/products" active={location.pathname === '/products'}>
              Produtos
            </Nav.Link>
            <Nav.Link as={Link} to="/raw-materials" active={location.pathname === '/raw-materials'}>
              Matérias-Primas
            </Nav.Link>
            <Nav.Link as={Link} to="/associations" active={location.pathname === '/associations'}>
              Associações
            </Nav.Link>
            <Nav.Link as={Link} to="/suggestions" active={location.pathname === '/suggestions'}>
              Sugestões
            </Nav.Link>
          </Nav>
        </BsNavbar.Collapse>
      </Container>
    </BsNavbar>
  );
}
