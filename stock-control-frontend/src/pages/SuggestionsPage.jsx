import { Container } from 'react-bootstrap';
import ProductionSuggestion from '../components/ProductionSuggestion';

export default function SuggestionsPage() {
  return (
    <Container>
      <h2 className="mb-3">Sugestão de Produção</h2>
      <ProductionSuggestion />
    </Container>
  );
}
