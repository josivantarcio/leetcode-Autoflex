import { useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import ProductRawMaterialList from '../components/ProductRawMaterialList';
import ProductRawMaterialForm from '../components/ProductRawMaterialForm';

export default function AssociationsPage() {
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);

  const handleEdit = (assoc) => {
    setEditing(assoc);
    setShowForm(true);
  };

  const handleHide = () => {
    setShowForm(false);
    setEditing(null);
  };

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Associações Produto - Matéria-Prima</h2>
        <Button onClick={() => setShowForm(true)}>Nova Associação</Button>
      </div>
      <ProductRawMaterialList onEdit={handleEdit} />
      <ProductRawMaterialForm show={showForm} onHide={handleHide} editing={editing} />
    </Container>
  );
}
