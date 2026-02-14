import { Table, Button, Spinner, Card } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { fetchSuggestions } from '../store/suggestionSlice';

export default function ProductionSuggestion() {
  const dispatch = useDispatch();
  const { data, status } = useSelector((state) => state.suggestions);

  return (
    <>
      <Button className="mb-3" onClick={() => dispatch(fetchSuggestions())} disabled={status === 'loading'}>
        {status === 'loading' ? <Spinner size="sm" animation="border" /> : 'Gerar Sugestão'}
      </Button>

      {data && (
        <>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Produto</th>
                <th>Quantidade</th>
                <th>Valor Unitário (R$)</th>
                <th>Subtotal (R$)</th>
              </tr>
            </thead>
            <tbody>
              {data.suggestedProducts.map((sp) => (
                <tr key={sp.productId}>
                  <td>{sp.productName}</td>
                  <td>{sp.quantity}</td>
                  <td>{parseFloat(sp.productValue).toFixed(2)}</td>
                  <td>{parseFloat(sp.subtotal).toFixed(2)}</td>
                </tr>
              ))}
              {data.suggestedProducts.length === 0 && (
                <tr><td colSpan="4" className="text-center">Nenhuma sugestão disponível.</td></tr>
              )}
            </tbody>
          </Table>

          <Card className="mt-3" style={{ maxWidth: '300px' }}>
            <Card.Body>
              <Card.Title>Valor Total</Card.Title>
              <Card.Text className="fs-4 fw-bold text-success">
                R$ {parseFloat(data.totalValue).toFixed(2)}
              </Card.Text>
            </Card.Body>
          </Card>
        </>
      )}
    </>
  );
}
