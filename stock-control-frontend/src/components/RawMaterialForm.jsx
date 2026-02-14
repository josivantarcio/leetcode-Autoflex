import { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { createRawMaterial, updateRawMaterial } from '../store/rawMaterialSlice';

export default function RawMaterialForm({ show, onHide, editing }) {
  const dispatch = useDispatch();
  const [name, setName] = useState('');
  const [stockQuantity, setStockQuantity] = useState('');

  useEffect(() => {
    if (editing) {
      setName(editing.name);
      setStockQuantity(editing.stockQuantity);
    } else {
      setName('');
      setStockQuantity('');
    }
  }, [editing, show]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const material = { name, stockQuantity: parseFloat(stockQuantity) };
    if (editing) {
      dispatch(updateRawMaterial({ ...material, id: editing.id }));
    } else {
      dispatch(createRawMaterial(material));
    }
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Editar Matéria-Prima' : 'Nova Matéria-Prima'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Nome</Form.Label>
            <Form.Control required value={name} onChange={(e) => setName(e.target.value)} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Quantidade em Estoque</Form.Label>
            <Form.Control required type="number" step="0.01" min="0" value={stockQuantity} onChange={(e) => setStockQuantity(e.target.value)} />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>Cancelar</Button>
          <Button variant="primary" type="submit">Salvar</Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}
