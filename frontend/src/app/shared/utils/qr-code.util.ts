/**
 * Utilitário autônomo para geração de QR Code em formato SVG (sem dependências externas).
 * Implementa especificação QR Code (ISO/IEC 18004) para codificação em modo Byte (8-bit)
 * com correção de erro (Reed-Solomon GF(256)).
 */

// Tabela de log e antilog de Galois Field 256 com polinômio 0x11d
const GF256_EXP = new Uint8Array(512);
const GF256_LOG = new Uint8Array(256);

(() => {
  let x = 1;
  for (let i = 0; i < 255; i++) {
    GF256_EXP[i] = x;
    GF256_EXP[i + 255] = x;
    GF256_LOG[x] = i;
    x <<= 1;
    if (x & 0x100) x ^= 0x11d;
  }
})();

function gfMul(x: number, y: number): number {
  if (x === 0 || y === 0) return 0;
  return GF256_EXP[GF256_LOG[x] + GF256_LOG[y]];
}

function polyMul(p1: Uint8Array, p2: Uint8Array): Uint8Array {
  const res = new Uint8Array(p1.length + p2.length - 1);
  for (let i = 0; i < p1.length; i++) {
    for (let j = 0; j < p2.length; j++) {
      res[i + j] ^= gfMul(p1[i], p2[j]);
    }
  }
  return new Uint8Array(res.buffer, res.byteOffset, res.byteLength);
}

function getGeneratorPoly(degree: number): Uint8Array {
  let g: Uint8Array = new Uint8Array([1]);
  for (let i = 0; i < degree; i++) {
    g = polyMul(g, new Uint8Array([1, GF256_EXP[i]]));
  }
  return g;
}

function calculateEcc(data: Uint8Array, eccLen: number): Uint8Array {
  const gen = getGeneratorPoly(eccLen);
  const msg = new Uint8Array(data.length + eccLen);
  msg.set(data);

  for (let i = 0; i < data.length; i++) {
    const coef = msg[i];
    if (coef !== 0) {
      for (let j = 0; j < gen.length; j++) {
        msg[i + j] ^= gfMul(gen[j], coef);
      }
    }
  }
  const ecc = msg.subarray(data.length);
  return new Uint8Array(ecc);
}

// Capacidade e blocos para ECC nível M (Medium - 15% recuperação)
interface VersionInfo {
  version: number;
  totalCodewords: number;
  dataCodewords: number;
  eccCodewordsPerBlock: number;
  numBlocksGroup1: number;
  dataCodewordsBlock1: number;
  numBlocksGroup2: number;
  dataCodewordsBlock2: number;
  alignmentPatterns: number[];
}

const VERSIONS: VersionInfo[] = [
  { version: 1, totalCodewords: 26, dataCodewords: 16, eccCodewordsPerBlock: 10, numBlocksGroup1: 1, dataCodewordsBlock1: 16, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [] },
  { version: 2, totalCodewords: 44, dataCodewords: 28, eccCodewordsPerBlock: 16, numBlocksGroup1: 1, dataCodewordsBlock1: 28, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 18] },
  { version: 3, totalCodewords: 70, dataCodewords: 44, eccCodewordsPerBlock: 26, numBlocksGroup1: 1, dataCodewordsBlock1: 44, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 22] },
  { version: 4, totalCodewords: 100, dataCodewords: 64, eccCodewordsPerBlock: 18, numBlocksGroup1: 2, dataCodewordsBlock1: 32, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 26] },
  { version: 5, totalCodewords: 134, dataCodewords: 86, eccCodewordsPerBlock: 24, numBlocksGroup1: 2, dataCodewordsBlock1: 43, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 30] },
  { version: 6, totalCodewords: 172, dataCodewords: 108, eccCodewordsPerBlock: 16, numBlocksGroup1: 4, dataCodewordsBlock1: 27, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 34] },
  { version: 7, totalCodewords: 196, dataCodewords: 124, eccCodewordsPerBlock: 18, numBlocksGroup1: 4, dataCodewordsBlock1: 31, numBlocksGroup2: 0, dataCodewordsBlock2: 0, alignmentPatterns: [6, 22, 38] },
  { version: 8, totalCodewords: 242, dataCodewords: 154, eccCodewordsPerBlock: 22, numBlocksGroup1: 2, dataCodewordsBlock1: 38, numBlocksGroup2: 2, dataCodewordsBlock2: 39, alignmentPatterns: [6, 24, 42] },
  { version: 9, totalCodewords: 292, dataCodewords: 182, eccCodewordsPerBlock: 22, numBlocksGroup1: 3, dataCodewordsBlock1: 36, numBlocksGroup2: 2, dataCodewordsBlock2: 37, alignmentPatterns: [6, 26, 46] },
  { version: 10, totalCodewords: 346, dataCodewords: 216, eccCodewordsPerBlock: 26, numBlocksGroup1: 4, dataCodewordsBlock1: 40, numBlocksGroup2: 1, dataCodewordsBlock2: 41, alignmentPatterns: [6, 28, 50] },
];

export class QrCodeGenerator {
  public static generateSvg(text: string, size = 200): string {
    const matrix = this.createMatrix(text);
    const n = matrix.length;
    const cellSize = size / (n + 8); // 4 módulos de margem (quiet zone)
    const margin = cellSize * 4;

    let paths = '';
    for (let r = 0; r < n; r++) {
      for (let c = 0; c < n; c++) {
        if (matrix[r][c]) {
          const x = margin + c * cellSize;
          const y = margin + r * cellSize;
          paths += `M${x.toFixed(2)},${y.toFixed(2)}h${cellSize.toFixed(2)}v${cellSize.toFixed(2)}h-${cellSize.toFixed(2)}z `;
        }
      }
    }

    return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${size} ${size}" width="100%" height="100%" role="img" aria-label="QR Code TOTP">
      <rect width="100%" height="100%" fill="#ffffff" rx="8" />
      <path d="${paths.trim()}" fill="#111827" />
    </svg>`;
  }

  public static createMatrix(text: string): boolean[][] {
    const utf8Bytes = new TextEncoder().encode(text);
    const dataLen = utf8Bytes.length;

    let verInfo: VersionInfo | null = null;
    for (const v of VERSIONS) {
      // 4 bits modo + (8 ou 16) bits contador de tamanho + dados
      const countBits = v.version < 10 ? 8 : 16;
      const totalBits = 4 + countBits + dataLen * 8;
      const totalBytesNeeded = Math.ceil(totalBits / 8);
      if (totalBytesNeeded <= v.dataCodewords) {
        verInfo = v;
        break;
      }
    }

    if (!verInfo) {
      // Fallback para maior versão suportada
      verInfo = VERSIONS[VERSIONS.length - 1];
    }

    // Codificação de dados
    const bits: number[] = [];
    const pushBits = (val: number, len: number) => {
      for (let i = len - 1; i >= 0; i--) {
        bits.push((val >> i) & 1);
      }
    };

    // Modo Byte: 0100
    pushBits(0b0100, 4);
    // Contador de tamanho
    pushBits(dataLen, verInfo.version < 10 ? 8 : 16);
    // Dados UTF-8
    for (const b of utf8Bytes) {
      pushBits(b, 8);
    }
    // Terminador (até 4 bits 0)
    const maxBits = verInfo.dataCodewords * 8;
    const termLen = Math.min(4, maxBits - bits.length);
    pushBits(0, termLen);
    // Preenchimento de bits até múltiplo de 8
    while (bits.length % 8 !== 0) {
      bits.push(0);
    }

    // Converte para codewords
    const dataCodewords: number[] = [];
    for (let i = 0; i < bits.length; i += 8) {
      let byte = 0;
      for (let j = 0; j < 8; j++) {
        byte = (byte << 1) | bits[i + j];
      }
      dataCodewords.push(byte);
    }

    // Preenchimento de bytes alternados (0xEC, 0x11)
    const padBytes = [0xec, 0x11];
    let padIdx = 0;
    while (dataCodewords.length < verInfo.dataCodewords) {
      dataCodewords.push(padBytes[padIdx % 2]);
      padIdx++;
    }

    // Dividir em blocos e calcular ECC
    const blocks: { data: Uint8Array; ecc: Uint8Array }[] = [];
    let offset = 0;
    for (let i = 0; i < verInfo.numBlocksGroup1; i++) {
      const bData = new Uint8Array(dataCodewords.slice(offset, offset + verInfo.dataCodewordsBlock1));
      offset += verInfo.dataCodewordsBlock1;
      const bEcc = calculateEcc(bData, verInfo.eccCodewordsPerBlock);
      blocks.push({ data: bData, ecc: bEcc });
    }
    for (let i = 0; i < verInfo.numBlocksGroup2; i++) {
      const bData = new Uint8Array(dataCodewords.slice(offset, offset + verInfo.dataCodewordsBlock2));
      offset += verInfo.dataCodewordsBlock2;
      const bEcc = calculateEcc(bData, verInfo.eccCodewordsPerBlock);
      blocks.push({ data: bData, ecc: bEcc });
    }

    // Intercalação de dados e ECC
    const interleaved: number[] = [];
    const maxDataLen = Math.max(...blocks.map((b) => b.data.length));
    for (let i = 0; i < maxDataLen; i++) {
      for (const b of blocks) {
        if (i < b.data.length) interleaved.push(b.data[i]);
      }
    }
    for (let i = 0; i < verInfo.eccCodewordsPerBlock; i++) {
      for (const b of blocks) {
        interleaved.push(b.ecc[i]);
      }
    }

    // Construção da Matriz
    const dim = 17 + 4 * verInfo.version;
    const matrix: (boolean | null)[][] = Array.from({ length: dim }, () => Array(dim).fill(null));
    const isReserved: boolean[][] = Array.from({ length: dim }, () => Array(dim).fill(false));

    // Padrões de Localização (Finder Patterns)
    const setFinder = (row: number, col: number) => {
      for (let r = -1; r <= 7; r++) {
        for (let c = -1; c <= 7; c++) {
          const mr = row + r;
          const mc = col + c;
          if (mr >= 0 && mr < dim && mc >= 0 && mc < dim) {
            isReserved[mr][mc] = true;
            if (r >= 0 && r <= 6 && c >= 0 && c <= 6) {
              const isBlack = r === 0 || r === 6 || c === 0 || c === 6 || (r >= 2 && r <= 4 && c >= 2 && c <= 4);
              matrix[mr][mc] = isBlack;
            } else {
              matrix[mr][mc] = false; // Separador
            }
          }
        }
      }
    };

    setFinder(0, 0);
    setFinder(0, dim - 7);
    setFinder(dim - 7, 0);

    // Padrões de Sincronização (Timing Patterns)
    for (let i = 8; i < dim - 8; i++) {
      if (!isReserved[6][i]) {
        matrix[6][i] = i % 2 === 0;
        isReserved[6][i] = true;
      }
      if (!isReserved[i][6]) {
        matrix[i][6] = i % 2 === 0;
        isReserved[i][6] = true;
      }
    }

    // Padrões de Alinhamento (Alignment Patterns)
    const alignCoords = verInfo.alignmentPatterns;
    for (let i = 0; i < alignCoords.length; i++) {
      for (let j = 0; j < alignCoords.length; j++) {
        const ar = alignCoords[i];
        const ac = alignCoords[j];
        if (isReserved[ar][ac]) continue;

        for (let r = -2; r <= 2; r++) {
          for (let c = -2; c <= 2; c++) {
            isReserved[ar + r][ac + c] = true;
            matrix[ar + r][ac + c] = Math.max(Math.abs(r), Math.abs(c)) !== 1;
          }
        }
      }
    }

    // Módulo escuro fixo
    matrix[4 * verInfo.version + 9][8] = true;
    isReserved[4 * verInfo.version + 9][8] = true;

    // Reservar áreas de Informação de Formato
    for (let i = 0; i < 9; i++) {
      isReserved[8][i] = true;
      isReserved[i][8] = true;
      isReserved[8][dim - 1 - i] = true;
      isReserved[dim - 1 - i][8] = true;
    }

    // Preenchimento de bits na matriz em zigue-zague
    const finalBits: number[] = [];
    for (const byte of interleaved) {
      for (let i = 7; i >= 0; i--) {
        finalBits.push((byte >> i) & 1);
      }
    }

    let bitIdx = 0;
    let upward = true;
    for (let c = dim - 1; c > 0; c -= 2) {
      if (c === 6) c--; // Pular timing pattern vertical
      for (let r = 0; r < dim; r++) {
        const row = upward ? dim - 1 - r : r;
        for (let colOffset = 0; colOffset < 2; colOffset++) {
          const col = c - colOffset;
          if (!isReserved[row][col]) {
            const val = bitIdx < finalBits.length ? finalBits[bitIdx++] : 0;
            // Aplicar máscara 0: (row + col) % 2 === 0
            const mask = (row + col) % 2 === 0;
            matrix[row][col] = (val === 1) !== mask;
          }
        }
      }
      upward = !upward;
    }

    // Informação de Formato (Nível M, Máscara 0 = 0b00 + 0b000 = 0b00000 -> BCH format bits = 0x5412)
    // Constante BCH format bits para M + Mask 0: 101010000010010 (0x5412)
    const FORMAT_INFO = 0x5412;
    for (let i = 0; i < 15; i++) {
      const bit = ((FORMAT_INFO >> i) & 1) === 1;
      // Ao redor do finder top-left
      if (i < 6) matrix[8][i] = bit;
      else if (i === 6) matrix[8][7] = bit;
      else if (i === 7) matrix[8][8] = bit;
      else if (i === 8) matrix[7][8] = bit;
      else matrix[14 - i][8] = bit;

      // Ao redor dos finders top-right e bottom-left
      if (i < 8) matrix[dim - 1 - i][8] = bit;
      else matrix[8][dim - 15 + i] = bit;
    }

    return matrix.map((row) => row.map((cell) => cell === true));
  }
}
