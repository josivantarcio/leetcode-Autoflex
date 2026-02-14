import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { fetchProducts, deleteProduct } from '../store/productSlice';

export default function ProductList({ onEdit }) {
  const dispatch = useDispatch();
  const { items, status, error } = useSelector((state) => state.products);

  useEffect(() => {
    if (status === 'idle') dispatch(fetchProducts());
  }, [status, dispatch]);

  if (status === 'loading') return <Spinner animation="border" />;
  if (status === 'failed') return <Alert variant="danger">Erro ao carregar produtos: {error}. Verifique se o backend está rodando em localhost:8080.</Alert>;

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Valor (R$)</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {items.map((p) => (
          <tr key={p.id}>
            <td>{p.id}</td>
            <td>{p.name}</td>
            <td>{parseFloat(p.value).toFixed(2)}</td>
            <td>
              <Button size="sm" variant="warning" className="me-2" onClick={() => onEdit(p)}>Editar</Button>
              <Button size="sm" variant="danger" onClick={() => dispatch(deleteProduct(p.id))}>Excluir</Button>
            </td>
          </tr>
        ))}
        {items.length === 0 && (
          <tr><td colSpan="4" className="text-center">Nenhum produto cadastrado.</td></tr>
        )}
      </tbody>
    </Table>
  );
}
