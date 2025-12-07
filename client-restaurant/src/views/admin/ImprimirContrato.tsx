// ImprimirContrato.tsx
import React from "react";

interface Props {
  contratoData: any;
}

const ImprimirContrato: React.FC<Props> = ({ contratoData }) => {
  const handleImprimir = () => {
    if (!contratoData) return;

    const w = window.open("", "_blank");
    if (w) {
      w.document.write("<html><head><title>Contrato de Trabajo</title></head><body>");
      w.document.write(document.getElementById("contratoHtml")!.innerHTML);
      w.document.write("</body></html>");
      w.document.close();
      w.print();
    }
  };

  return (
    <button onClick={handleImprimir} style={{ marginLeft: "1rem", padding: "0.5rem 1rem", cursor: "pointer" }}>
      Ver/Imprimir Contrato
    </button>
  );
};

export default ImprimirContrato;
