import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { fetchRawMaterials, deleteRawMaterial } from '../store/rawMaterialSlice';

export default function RawMaterialList({ onEdit }) {
  const dispatch = useDispatch();
  const { items, status, error } = useSelector((state) => state.rawMaterials);

  useEffect(() => {
    if (status === 'idle') dispatch(fetchRawMaterials());
  }, [status, dispatch]);

  if (status === 'loading') return <Spinner animation="border" />;
  if (status === 'failed') return <Alert variant="danger">Erro ao carregar matérias-primas: {error}. Verifique se o backend está rodando em localhost:8080.</Alert>;

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Qtd. Estoque</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {items.map((m) => (
          <tr key={m.id}>
            <td>{m.id}</td>
            <td>{m.name}</td>
            <td>{parseFloat(m.stockQuantity).toFixed(2)}</td>
            <td>
              <Button size="sm" variant="warning" className="me-2" onClick={() => onEdit(m)}>Editar</Button>
              <Button size="sm" variant="danger" onClick={() => dispatch(deleteRawMaterial(m.id))}>Excluir</Button>
            </td>
          </tr>
        ))}
        {items.length === 0 && (
          <tr><td colSpan="4" className="text-center">Nenhuma matéria-prima cadastrada.</td></tr>
        )}
      </tbody>
    </Table>
  );
}
