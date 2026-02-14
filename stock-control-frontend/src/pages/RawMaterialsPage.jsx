import { useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import RawMaterialList from '../components/RawMaterialList';
import RawMaterialForm from '../components/RawMaterialForm';

export default function RawMaterialsPage() {
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);

  const handleEdit = (material) => {
    setEditing(material);
    setShowForm(true);
  };

  const handleHide = () => {
    setShowForm(false);
    setEditing(null);
  };

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Matérias-Primas</h2>
        <Button onClick={() => setShowForm(true)}>Nova Matéria-Prima</Button>
      </div>
      <RawMaterialList onEdit={handleEdit} />
      <RawMaterialForm show={showForm} onHide={handleHide} editing={editing} />
    </Container>
  );
}
