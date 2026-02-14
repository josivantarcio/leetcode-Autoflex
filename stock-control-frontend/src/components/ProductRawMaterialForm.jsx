import { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { createProductRawMaterial, updateProductRawMaterial } from '../store/productRawMaterialSlice';
import { fetchProducts } from '../store/productSlice';
import { fetchRawMaterials } from '../store/rawMaterialSlice';

export default function ProductRawMaterialForm({ show, onHide, editing }) {
  const dispatch = useDispatch();
  const products = useSelector((state) => state.products.items);
  const rawMaterials = useSelector((state) => state.rawMaterials.items);
  const productStatus = useSelector((state) => state.products.status);
  const rawMaterialStatus = useSelector((state) => state.rawMaterials.status);

  const [productId, setProductId] = useState('');
  const [rawMaterialId, setRawMaterialId] = useState('');
  const [quantity, setQuantity] = useState('');

  useEffect(() => {
    if (productStatus === 'idle') dispatch(fetchProducts());
    if (rawMaterialStatus === 'idle') dispatch(fetchRawMaterials());
  }, [productStatus, rawMaterialStatus, dispatch]);

  useEffect(() => {
    if (editing) {
      setProductId(editing.productId);
      setRawMaterialId(editing.rawMaterialId);
      setQuantity(editing.quantity);
    } else {
      setProductId('');
      setRawMaterialId('');
      setQuantity('');
    }
  }, [editing, show]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const assoc = {
      productId: parseInt(productId),
      rawMaterialId: parseInt(rawMaterialId),
      quantity: parseFloat(quantity),
    };
    if (editing) {
      dispatch(updateProductRawMaterial({ ...assoc, id: editing.id }));
    } else {
      dispatch(createProductRawMaterial(assoc));
    }
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Editar Associação' : 'Nova Associação'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Produto</Form.Label>
            <Form.Select required value={productId} onChange={(e) => setProductId(e.target.value)}>
              <option value="">Selecione...</option>
              {products.map((p) => (
                <option key={p.id} value={p.id}>{p.name}</option>
              ))}
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Matéria-Prima</Form.Label>
            <Form.Select required value={rawMaterialId} onChange={(e) => setRawMaterialId(e.target.value)}>
              <option value="">Selecione...</option>
              {rawMaterials.map((m) => (
                <option key={m.id} value={m.id}>{m.name}</option>
              ))}
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Quantidade</Form.Label>
            <Form.Control required type="number" step="0.01" min="0.01" value={quantity} onChange={(e) => setQuantity(e.target.value)} />
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
