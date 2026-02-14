import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { fetchProductRawMaterials, deleteProductRawMaterial } from '../store/productRawMaterialSlice';

export default function ProductRawMaterialList({ onEdit }) {
  const dispatch = useDispatch();
  const { items, status, error } = useSelector((state) => state.productRawMaterials);

  useEffect(() => {
    if (status === 'idle') dispatch(fetchProductRawMaterials());
  }, [status, dispatch]);

  if (status === 'loading') return <Spinner animation="border" />;
  if (status === 'failed') return <Alert variant="danger">Erro ao carregar associações: {error}. Verifique se o backend está rodando em localhost:8080.</Alert>;

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>ID</th>
          <th>Produto</th>
          <th>Matéria-Prima</th>
          <th>Quantidade</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {items.map((a) => (
          <tr key={a.id}>
            <td>{a.id}</td>
            <td>{a.productName}</td>
            <td>{a.rawMaterialName}</td>
            <td>{parseFloat(a.quantity).toFixed(2)}</td>
            <td>
              <Button size="sm" variant="warning" className="me-2" onClick={() => onEdit(a)}>Editar</Button>
              <Button size="sm" variant="danger" onClick={() => dispatch(deleteProductRawMaterial(a.id))}>Excluir</Button>
            </td>
          </tr>
        ))}
        {items.length === 0 && (
          <tr><td colSpan="5" className="text-center">Nenhuma associação cadastrada.</td></tr>
        )}
      </tbody>
    </Table>
  );
}
