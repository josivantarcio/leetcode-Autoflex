import { useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import ProductList from '../components/ProductList';
import ProductForm from '../components/ProductForm';

export default function ProductsPage() {
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);

  const handleEdit = (product) => {
    setEditing(product);
    setShowForm(true);
  };

  const handleHide = () => {
    setShowForm(false);
    setEditing(null);
  };

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Produtos</h2>
        <Button onClick={() => setShowForm(true)}>Novo Produto</Button>
      </div>
      <ProductList onEdit={handleEdit} />
      <ProductForm show={showForm} onHide={handleHide} editing={editing} />
    </Container>
  );
}
