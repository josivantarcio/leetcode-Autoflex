import { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { createProduct, updateProduct } from '../store/productSlice';

export default function ProductForm({ show, onHide, editing }) {
  const dispatch = useDispatch();
  const [name, setName] = useState('');
  const [value, setValue] = useState('');

  useEffect(() => {
    if (editing) {
      setName(editing.name);
      setValue(editing.value);
    } else {
      setName('');
      setValue('');
    }
  }, [editing, show]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const product = { name, value: parseFloat(value) };
    if (editing) {
      dispatch(updateProduct({ ...product, id: editing.id }));
    } else {
      dispatch(createProduct(product));
    }
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Editar Produto' : 'Novo Produto'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Nome</Form.Label>
            <Form.Control required value={name} onChange={(e) => setName(e.target.value)} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Valor (R$)</Form.Label>
            <Form.Control required type="number" step="0.01" min="0.01" value={value} onChange={(e) => setValue(e.target.value)} />
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
