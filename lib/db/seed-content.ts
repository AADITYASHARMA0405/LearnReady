/**
 * Seed content for LearnReady App demo
 * Subjects, modules, lessons, and questions in English + Kannada
 */

export const SEED_SUBJECTS_SQL = `
INSERT OR IGNORE INTO subjects (id, name_en, name_kn, stream, icon_name, color) VALUES
  (1, 'Mathematics', 'ಗಣಿತ', 'engineering', 'calculator-outline', '#BFDBFE'),
  (2, 'Computer Science', 'ಕಂಪ್ಯೂಟರ್ ವಿಜ್ಞಾನ', 'engineering', 'laptop-outline', '#E9D5FF'),
  (3, 'Physics', 'ಭೌತಶಾಸ್ತ್ರ', 'engineering', 'planet-outline', '#BBF7D0'),
  (4, 'Mechanical', 'ಯಾಂತ್ರಿಕ', 'engineering', 'cog-outline', '#FED7AA');
`;

export const SEED_MODULES_SQL = `
INSERT OR IGNORE INTO modules (id, subject_id, title_en, title_kn, description_en, description_kn, order_index, type, difficulty) VALUES
  -- Math modules
  (1, 1, 'Linear Algebra Basics', 'ರೇಖೀಯ ಬೀಜಗಣಿತ ಮೂಲಗಳು', 'Matrices, determinants and linear equations', 'ಮ್ಯಾಟ್ರಿಕ್ಸ್, ಡಿಟರ್ಮಿನೆಂಟ್ಸ್ ಮತ್ತು ರೇಖೀಯ ಸಮೀಕರಣಗಳು', 1, 'lesson', 'easy'),
  (2, 1, 'Calculus Fundamentals', 'ಕಲನಶಾಸ್ತ್ರ ಮೂಲಗಳು', 'Differentiation and integration basics', 'ವಿಭೇದನ ಮತ್ತು ಸಮಾಕಲನ ಮೂಲಗಳು', 2, 'lesson', 'medium'),
  (3, 1, 'Probability & Statistics', 'ಸಂಭಾವ್ಯತೆ ಮತ್ತು ಅಂಕಿಅಂಶ', 'Probability theory and data analysis', 'ಸಂಭಾವ್ಯತಾ ಸಿದ್ಧಾಂತ ಮತ್ತು ಡೇಟಾ ವಿಶ್ಲೇಷಣೆ', 3, 'lesson', 'medium'),

  -- CS modules
  (4, 2, 'Data Structures', 'ಡೇಟಾ ರಚನೆಗಳು', 'Arrays, linked lists, stacks and queues', 'ಅರೇಗಳು, ಲಿಂಕ್ಡ್ ಲಿಸ್ಟ್‌ಗಳು, ಸ್ಟಾಕ್‌ಗಳು ಮತ್ತು ಕ್ಯೂಗಳು', 1, 'lesson', 'easy'),
  (5, 2, 'Algorithms', 'ಕ್ರಮಾವಳಿಗಳು', 'Sorting, searching and graph algorithms', 'ವಿಂಗಡಣೆ, ಹುಡುಕಾಟ ಮತ್ತು ಗ್ರಾಫ್ ಕ್ರಮಾವಳಿಗಳು', 2, 'lesson', 'medium'),
  (6, 2, 'Database Systems', 'ಡೇಟಾಬೇಸ್ ವ್ಯವಸ್ಥೆಗಳು', 'SQL, normalization and ER diagrams', 'SQL, ಸಾಮಾನ್ಯೀಕರಣ ಮತ್ತು ER ರೇಖಾಚಿತ್ರಗಳು', 3, 'lesson', 'medium'),

  -- Physics modules
  (7, 3, 'Mechanics', 'ಯಂತ್ರಶಾಸ್ತ್ರ', 'Newton laws, motion and forces', 'ನ್ಯೂಟನ್ ನಿಯಮಗಳು, ಚಲನೆ ಮತ್ತು ಬಲಗಳು', 1, 'lesson', 'easy'),
  (8, 3, 'Thermodynamics', 'ಉಷ್ಣಬಲಶಾಸ್ತ್ರ', 'Heat, energy and laws of thermodynamics', 'ಶಾಖ, ಶಕ್ತಿ ಮತ್ತು ಉಷ್ಣಬಲಶಾಸ್ತ್ರದ ನಿಯಮಗಳು', 2, 'lesson', 'medium'),
  (9, 3, 'Electromagnetism', 'ವಿದ್ಯುತ್‌ಕಾಂತೀಯತೆ', 'Electric fields, circuits and magnetism', 'ವಿದ್ಯುತ್ ಕ್ಷೇತ್ರಗಳು, ಸರ್ಕ್ಯೂಟ್‌ಗಳು ಮತ್ತು ಕಾಂತೀಯತೆ', 3, 'lesson', 'hard'),

  -- Mechanical modules
  (10, 4, 'Engineering Drawing', 'ಎಂಜಿನಿಯರಿಂಗ್ ಡ್ರಾಯಿಂಗ್', 'Orthographic projections and sections', 'ಲಂಬಕೋನ ಪ್ರಕ್ಷೇಪಣೆಗಳು ಮತ್ತು ವಿಭಾಗಗಳು', 1, 'lesson', 'easy'),
  (11, 4, 'Strength of Materials', 'ವಸ್ತುಗಳ ಬಲ', 'Stress, strain and beam bending', 'ಒತ್ತಡ, ವಿಕೃತಿ ಮತ್ತು ಕಿರಣ ಬಾಗುವಿಕೆ', 2, 'lesson', 'medium'),
  (12, 4, 'Fluid Mechanics', 'ದ್ರವ ಯಂತ್ರಶಾಸ್ತ್ರ', 'Fluid properties, flow and Bernoulli theorem', 'ದ್ರವ ಗುಣಲಕ್ಷಣಗಳು, ಹರಿವು ಮತ್ತು ಬರ್ನೌಲಿ ಪ್ರಮೇಯ', 3, 'lesson', 'hard');
`;

export const SEED_LESSONS_SQL = `
INSERT OR IGNORE INTO lessons (id, module_id, title_en, title_kn, content_en, content_kn, key_points_en, key_points_kn, order_index) VALUES
  -- Linear Algebra lessons
  (1, 1, 'Introduction to Matrices', 'ಮ್ಯಾಟ್ರಿಕ್ಸ್‌ಗಳ ಪರಿಚಯ',
    'A matrix is a rectangular array of numbers arranged in rows and columns. Matrices are used in many areas of mathematics and engineering.\n\nA matrix with m rows and n columns is called an m×n matrix. For example, a 2×3 matrix has 2 rows and 3 columns.\n\nTypes of Matrices:\n• Row Matrix: Has only one row\n• Column Matrix: Has only one column\n• Square Matrix: Same number of rows and columns\n• Zero Matrix: All elements are zero\n• Identity Matrix: Diagonal elements are 1, all others are 0\n\nMatrix Addition:\nTwo matrices can be added only if they have the same dimensions. Add corresponding elements.\n\nMatrix Multiplication:\nTo multiply matrices A (m×n) and B (n×p), the number of columns of A must equal the number of rows of B. The result is an m×p matrix.',
    'ಮ್ಯಾಟ್ರಿಕ್ಸ್ ಎಂದರೆ ಸಾಲುಗಳು ಮತ್ತು ಕಾಲಮ್‌ಗಳಲ್ಲಿ ಜೋಡಿಸಲಾದ ಸಂಖ್ಯೆಗಳ ಆಯತಾಕಾರದ ಜೋಡಣೆ. ಗಣಿತ ಮತ್ತು ಎಂಜಿನಿಯರಿಂಗ್‌ನ ಅನೇಕ ಕ್ಷೇತ್ರಗಳಲ್ಲಿ ಮ್ಯಾಟ್ರಿಕ್ಸ್‌ಗಳನ್ನು ಬಳಸಲಾಗುತ್ತದೆ.\n\nm ಸಾಲುಗಳು ಮತ್ತು n ಕಾಲಮ್‌ಗಳನ್ನು ಹೊಂದಿರುವ ಮ್ಯಾಟ್ರಿಕ್ಸ್ ಅನ್ನು m×n ಮ್ಯಾಟ್ರಿಕ್ಸ್ ಎಂದು ಕರೆಯಲಾಗುತ್ತದೆ.',
    'Matrices are rectangular arrays of numbers|Types: Row, Column, Square, Zero, Identity|Addition requires same dimensions|Multiplication: columns of A must equal rows of B',
    'ಮ್ಯಾಟ್ರಿಕ್ಸ್‌ಗಳು ಸಂಖ್ಯೆಗಳ ಆಯತಾಕಾರದ ಜೋಡಣೆಗಳು|ವಿಧಗಳು: ಸಾಲು, ಕಾಲಮ್, ಚೌಕ, ಶೂನ್ಯ, ಗುರುತಿನ|ಸೇರ್ಪಡೆಗೆ ಒಂದೇ ಆಯಾಮಗಳು ಬೇಕು',
    1),
  (2, 1, 'Determinants and Inverse', 'ಡಿಟರ್ಮಿನೆಂಟ್ಸ್ ಮತ್ತು ವ್ಯುತ್ಕ್ರಮ',
    'The determinant is a scalar value that can be computed from elements of a square matrix. It helps determine if a matrix is invertible.\n\nFor a 2×2 matrix [[a,b],[c,d]], the determinant = ad - bc.\n\nProperties:\n• If det(A) = 0, the matrix is singular (no inverse exists)\n• det(AB) = det(A) × det(B)\n• det(A^T) = det(A)\n\nInverse of a Matrix:\nA matrix A has an inverse A⁻¹ if and only if det(A) ≠ 0.\nFor a 2×2 matrix, A⁻¹ = (1/det(A)) × [[d,-b],[-c,a]]',
    'ಡಿಟರ್ಮಿನೆಂಟ್ ಎಂದರೆ ಒಂದು ಚೌಕ ಮ್ಯಾಟ್ರಿಕ್ಸ್‌ನ ಅಂಶಗಳಿಂದ ಲೆಕ್ಕ ಹಾಕಬಹುದಾದ ಸ್ಕೇಲಾರ್ ಮೌಲ್ಯ.',
    'Determinant tells if matrix is invertible|det = 0 means singular matrix|Inverse exists only when det ≠ 0',
    'ಡಿಟರ್ಮಿನೆಂಟ್ ಮ್ಯಾಟ್ರಿಕ್ಸ್ ವ್ಯುತ್ಕ್ರಮಿಸಬಹುದೇ ಎಂದು ಹೇಳುತ್ತದೆ',
    2),

  -- Data Structures lessons
  (3, 4, 'Arrays and Linked Lists', 'ಅರೇಗಳು ಮತ್ತು ಲಿಂಕ್ಡ್ ಲಿಸ್ಟ್‌ಗಳು',
    'Arrays are the simplest data structure. They store elements in contiguous memory locations.\n\nAdvantages of Arrays:\n• Fast access by index: O(1)\n• Simple to use\n• Cache-friendly\n\nDisadvantages:\n• Fixed size (in most languages)\n• Insertion/deletion is slow: O(n)\n\nLinked Lists store elements in nodes, where each node points to the next.\n\nAdvantages of Linked Lists:\n• Dynamic size\n• Easy insertion/deletion: O(1) at head\n\nDisadvantages:\n• No random access\n• Extra memory for pointers\n• Not cache-friendly',
    'ಅರೇಗಳು ಸರಳ ಡೇಟಾ ರಚನೆಗಳು. ಅವು ಅಂಶಗಳನ್ನು ಒಂದೇ ಸಾಲಿನ ಮೆಮೊರಿ ಸ್ಥಳಗಳಲ್ಲಿ ಸಂಗ್ರಹಿಸುತ್ತವೆ.',
    'Arrays: O(1) access, fixed size|Linked Lists: dynamic size, O(1) insertion at head|Arrays are cache-friendly, linked lists are not',
    'ಅರೇಗಳು: O(1) ಪ್ರವೇಶ, ಸ್ಥಿರ ಗಾತ್ರ|ಲಿಂಕ್ಡ್ ಲಿಸ್ಟ್: ಕ್ರಿಯಾತ್ಮಕ ಗಾತ್ರ',
    1),
  (4, 4, 'Stacks and Queues', 'ಸ್ಟಾಕ್‌ಗಳು ಮತ್ತು ಕ್ಯೂಗಳು',
    'A Stack follows Last In First Out (LIFO) principle. Think of a stack of plates.\n\nOperations:\n• Push: Add element to top\n• Pop: Remove element from top\n• Peek: View top element without removing\n\nApplications: Function calls, undo operations, expression evaluation.\n\nA Queue follows First In First Out (FIFO) principle. Think of a line at a store.\n\nOperations:\n• Enqueue: Add to rear\n• Dequeue: Remove from front\n• Peek: View front element\n\nApplications: Print queue, BFS, task scheduling.',
    'ಸ್ಟಾಕ್ ಕೊನೆಯಲ್ಲಿ ಬಂದದ್ದು ಮೊದಲು ಹೊರಗೆ (LIFO) ತತ್ವವನ್ನು ಅನುಸರಿಸುತ್ತದೆ.',
    'Stack: LIFO - Last In First Out|Queue: FIFO - First In First Out|Stack uses: function calls, undo|Queue uses: BFS, scheduling',
    'ಸ್ಟಾಕ್: LIFO|ಕ್ಯೂ: FIFO',
    2),

  -- Mechanics lessons
  (5, 7, 'Newton Laws of Motion', 'ನ್ಯೂಟನ್ ಚಲನೆಯ ನಿಯಮಗಳು',
    'Sir Isaac Newton formulated three laws of motion that form the foundation of classical mechanics.\n\nFirst Law (Law of Inertia):\nAn object at rest stays at rest, and an object in motion stays in motion with the same speed and direction, unless acted upon by an external force.\n\nSecond Law (F = ma):\nThe force acting on an object is equal to the mass of the object times its acceleration. F = ma.\n\nThird Law (Action-Reaction):\nFor every action, there is an equal and opposite reaction. When you push a wall, the wall pushes back with equal force.\n\nExamples:\n• A book on a table (First Law)\n• Pushing a cart (Second Law)\n• Rocket propulsion (Third Law)',
    'ಸರ್ ಐಸಾಕ್ ನ್ಯೂಟನ್ ಮೂರು ಚಲನೆಯ ನಿಯಮಗಳನ್ನು ರೂಪಿಸಿದರು.',
    'First Law: Objects resist change in motion|Second Law: F = ma|Third Law: Every action has equal opposite reaction',
    'ಮೊದಲ ನಿಯಮ: ಜಡತ್ವ|ಎರಡನೇ ನಿಯಮ: F = ma|ಮೂರನೇ ನಿಯಮ: ಕ್ರಿಯೆ-ಪ್ರತಿಕ್ರಿಯೆ',
    1),
  (6, 7, 'Work, Energy and Power', 'ಕೆಲಸ, ಶಕ್ತಿ ಮತ್ತು ಸಾಮರ್ಥ್ಯ',
    'Work is done when a force causes displacement. W = F × d × cos(θ)\n\nEnergy is the capacity to do work.\n\nKinetic Energy: KE = ½mv² (energy of moving objects)\nPotential Energy: PE = mgh (energy due to position)\n\nConservation of Energy:\nEnergy cannot be created or destroyed, only converted from one form to another.\n\nPower is the rate of doing work: P = W/t\nUnit: Watt (W) = Joule/second',
    'ಬಲವು ಸ್ಥಳಾಂತರವನ್ನು ಉಂಟುಮಾಡಿದಾಗ ಕೆಲಸ ಮಾಡಲಾಗುತ್ತದೆ.',
    'Work = Force × displacement × cos(θ)|KE = ½mv², PE = mgh|Energy is conserved|Power = Work/time',
    'ಕೆಲಸ = ಬಲ × ಸ್ಥಳಾಂತರ|KE = ½mv², PE = mgh|ಶಕ್ತಿ ಸಂರಕ್ಷಿಸಲ್ಪಡುತ್ತದೆ',
    2),

  -- Engineering Drawing lesson
  (7, 10, 'Orthographic Projections', 'ಲಂಬಕೋನ ಪ್ರಕ್ಷೇಪಣೆಗಳು',
    'Orthographic projection is a method of representing a 3D object in 2D. It uses parallel projection lines perpendicular to the projection plane.\n\nThree Principal Views:\n• Front View (Elevation)\n• Top View (Plan)\n• Side View (Profile)\n\nFirst Angle Projection:\n- Front view is placed above the top view\n- Used in India and Europe\n\nThird Angle Projection:\n- Top view is placed above the front view\n- Used in USA and Canada\n\nDimensioning Rules:\n• Place dimensions outside the view\n• Use arrows at both ends\n• Write dimensions above the dimension line',
    'ಲಂಬಕೋನ ಪ್ರಕ್ಷೇಪಣೆ ಎಂದರೆ 3D ವಸ್ತುವನ್ನು 2D ಯಲ್ಲಿ ಪ್ರತಿನಿಧಿಸುವ ವಿಧಾನ.',
    'Orthographic = 3D to 2D representation|Three views: Front, Top, Side|First Angle used in India|Third Angle used in USA',
    'ಲಂಬಕೋನ = 3D ಯಿಂದ 2D|ಮೂರು ನೋಟಗಳು: ಮುಂಭಾಗ, ಮೇಲ್ಭಾಗ, ಪಕ್ಕ',
    1);
`;

export const SEED_QUESTIONS_SQL = `
INSERT OR IGNORE INTO questions (id, module_id, subject_id, type, question_en, question_kn, options_en, options_kn, correct_answer, explanation_en, explanation_kn, difficulty) VALUES
  -- Math questions
  (1, 1, 1, 'mcq',
    'What is the determinant of a 2×2 matrix [[3,2],[1,4]]?',
    '2×2 ಮ್ಯಾಟ್ರಿಕ್ಸ್ [[3,2],[1,4]] ನ ಡಿಟರ್ಮಿನೆಂಟ್ ಏನು?',
    '["10","14","5","8"]',
    '["10","14","5","8"]',
    '10',
    'det = (3×4) - (2×1) = 12 - 2 = 10',
    'det = (3×4) - (2×1) = 12 - 2 = 10',
    2),
  (2, 1, 1, 'mcq',
    'Which matrix has all diagonal elements as 1 and others as 0?',
    'ಯಾವ ಮ್ಯಾಟ್ರಿಕ್ಸ್‌ನ ಎಲ್ಲಾ ಕರ್ಣೀಯ ಅಂಶಗಳು 1 ಮತ್ತು ಇತರವು 0?',
    '["Identity Matrix","Zero Matrix","Diagonal Matrix","Scalar Matrix"]',
    '["ಗುರುತಿನ ಮ್ಯಾಟ್ರಿಕ್ಸ್","ಶೂನ್ಯ ಮ್ಯಾಟ್ರಿಕ್ಸ್","ಕರ್ಣೀಯ ಮ್ಯಾಟ್ರಿಕ್ಸ್","ಸ್ಕೇಲಾರ್ ಮ್ಯಾಟ್ರಿಕ್ಸ್"]',
    'Identity Matrix',
    'An identity matrix (I) has 1s on the diagonal and 0s elsewhere. When multiplied with any matrix A, the result is A itself: AI = A.',
    'ಗುರುತಿನ ಮ್ಯಾಟ್ರಿಕ್ಸ್ (I) ಕರ್ಣೀಯದಲ್ಲಿ 1 ಮತ್ತು ಬೇರೆಡೆ 0 ಹೊಂದಿರುತ್ತದೆ.',
    1),
  (3, 2, 1, 'mcq',
    'What is the derivative of x²?',
    'x² ನ ಡೆರಿವೇಟಿವ್ ಏನು?',
    '["2x","x","2","x²"]',
    '["2x","x","2","x²"]',
    '2x',
    'Using the power rule: d/dx(xⁿ) = nxⁿ⁻¹. So d/dx(x²) = 2x¹ = 2x.',
    'ಪವರ್ ನಿಯಮ ಬಳಸಿ: d/dx(xⁿ) = nxⁿ⁻¹. ಆದ್ದರಿಂದ d/dx(x²) = 2x.',
    1),
  (4, 3, 1, 'mcq',
    'If P(A) = 0.3 and P(B) = 0.5, and A and B are independent, what is P(A∩B)?',
    'P(A) = 0.3 ಮತ್ತು P(B) = 0.5, A ಮತ್ತು B ಸ್ವತಂತ್ರವಾಗಿದ್ದರೆ, P(A∩B) ಏನು?',
    '["0.15","0.8","0.2","0.35"]',
    '["0.15","0.8","0.2","0.35"]',
    '0.15',
    'For independent events: P(A∩B) = P(A) × P(B) = 0.3 × 0.5 = 0.15',
    'ಸ್ವತಂತ್ರ ಘಟನೆಗಳಿಗೆ: P(A∩B) = P(A) × P(B) = 0.3 × 0.5 = 0.15',
    2),

  -- CS questions
  (5, 4, 2, 'mcq',
    'What is the time complexity of accessing an array element by index?',
    'ಸೂಚ್ಯಂಕದ ಮೂಲಕ ಅರೇ ಅಂಶವನ್ನು ಪ್ರವೇಶಿಸುವ ಸಮಯ ಸಂಕೀರ್ಣತೆ ಏನು?',
    '["O(1)","O(n)","O(log n)","O(n²)"]',
    '["O(1)","O(n)","O(log n)","O(n²)"]',
    'O(1)',
    'Arrays provide direct access by index through pointer arithmetic, so access time is constant O(1) regardless of array size.',
    'ಅರೇಗಳು ಪಾಯಿಂಟರ್ ಅಂಕಗಣಿತದ ಮೂಲಕ ನೇರ ಪ್ರವೇಶವನ್ನು ಒದಗಿಸುತ್ತವೆ.',
    1),
  (6, 4, 2, 'mcq',
    'Which data structure follows LIFO principle?',
    'ಯಾವ ಡೇಟಾ ರಚನೆ LIFO ತತ್ವವನ್ನು ಅನುಸರಿಸುತ್ತದೆ?',
    '["Stack","Queue","Array","Linked List"]',
    '["ಸ್ಟಾಕ್","ಕ್ಯೂ","ಅರೇ","ಲಿಂಕ್ಡ್ ಲಿಸ್ಟ್"]',
    'Stack',
    'Stack follows Last In First Out (LIFO). The last element added is the first one removed, like a stack of plates.',
    'ಸ್ಟಾಕ್ ಕೊನೆಯಲ್ಲಿ ಬಂದದ್ದು ಮೊದಲು ಹೊರಗೆ (LIFO) ಅನುಸರಿಸುತ್ತದೆ.',
    1),
  (7, 5, 2, 'mcq',
    'What is the time complexity of binary search?',
    'ಬೈನರಿ ಹುಡುಕಾಟದ ಸಮಯ ಸಂಕೀರ್ಣತೆ ಏನು?',
    '["O(log n)","O(n)","O(n log n)","O(1)"]',
    '["O(log n)","O(n)","O(n log n)","O(1)"]',
    'O(log n)',
    'Binary search halves the search space with each comparison, so it takes O(log n) time for a sorted array of n elements.',
    'ಬೈನರಿ ಹುಡುಕಾಟ ಪ್ರತಿ ಹೋಲಿಕೆಯಲ್ಲಿ ಹುಡುಕಾಟ ಜಾಗವನ್ನು ಅರ್ಧ ಮಾಡುತ್ತದೆ.',
    2),
  (8, 6, 2, 'mcq',
    'What does SQL stand for?',
    'SQL ಎಂದರೆ ಏನು?',
    '["Structured Query Language","Simple Query Language","Standard Query Logic","System Query Language"]',
    '["Structured Query Language","Simple Query Language","Standard Query Logic","System Query Language"]',
    'Structured Query Language',
    'SQL stands for Structured Query Language. It is used to communicate with and manage relational databases.',
    'SQL ಎಂದರೆ Structured Query Language. ಇದನ್ನು ಸಂಬಂಧಿತ ಡೇಟಾಬೇಸ್‌ಗಳನ್ನು ನಿರ್ವಹಿಸಲು ಬಳಸಲಾಗುತ್ತದೆ.',
    1),

  -- Physics questions
  (9, 7, 3, 'mcq',
    'According to Newton''s second law, F equals:',
    'ನ್ಯೂಟನ್ ಎರಡನೇ ನಿಯಮದ ಪ್ರಕಾರ, F ಸಮ:',
    '["ma","mv","mg","mgh"]',
    '["ma","mv","mg","mgh"]',
    'ma',
    'Newton''s second law states that Force equals mass times acceleration: F = ma.',
    'ನ್ಯೂಟನ್ ಎರಡನೇ ನಿಯಮ: ಬಲ = ದ್ರವ್ಯರಾಶಿ × ವೇಗವರ್ಧನೆ: F = ma.',
    1),
  (10, 7, 3, 'mcq',
    'What is the SI unit of force?',
    'ಬಲದ SI ಘಟಕ ಯಾವುದು?',
    '["Newton","Joule","Watt","Pascal"]',
    '["ನ್ಯೂಟನ್","ಜೂಲ್","ವಾಟ್","ಪ್ಯಾಸ್ಕಲ್"]',
    'Newton',
    'The SI unit of force is Newton (N). 1 Newton = 1 kg × m/s².',
    'ಬಲದ SI ಘಟಕ ನ್ಯೂಟನ್ (N). 1 ನ್ಯೂಟನ್ = 1 kg × m/s².',
    1),
  (11, 8, 3, 'mcq',
    'The first law of thermodynamics is about:',
    'ಉಷ್ಣಬಲಶಾಸ್ತ್ರದ ಮೊದಲ ನಿಯಮ ಇದರ ಬಗ್ಗೆ:',
    '["Conservation of energy","Entropy","Absolute zero","Heat transfer"]',
    '["ಶಕ್ತಿ ಸಂರಕ್ಷಣೆ","ಎಂಟ್ರೋಪಿ","ಸಂಪೂರ್ಣ ಶೂನ್ಯ","ಶಾಖ ವರ್ಗಾವಣೆ"]',
    'Conservation of energy',
    'The first law of thermodynamics states that energy cannot be created or destroyed, only transformed from one form to another.',
    'ಉಷ್ಣಬಲಶಾಸ್ತ್ರದ ಮೊದಲ ನಿಯಮ: ಶಕ್ತಿಯನ್ನು ಸೃಷ್ಟಿಸಲು ಅಥವಾ ನಾಶಮಾಡಲು ಸಾಧ್ಯವಿಲ್ಲ.',
    2),

  -- Mechanical questions
  (12, 10, 4, 'mcq',
    'In First Angle Projection, the front view is placed:',
    'ಮೊದಲ ಕೋನ ಪ್ರಕ್ಷೇಪಣೆಯಲ್ಲಿ, ಮುಂಭಾಗದ ನೋಟವನ್ನು ಇರಿಸಲಾಗುತ್ತದೆ:',
    '["Above the top view","Below the top view","Left of the side view","Right of the side view"]',
    '["ಮೇಲ್ನೋಟದ ಮೇಲೆ","ಮೇಲ್ನೋಟದ ಕೆಳಗೆ","ಪಕ್ಕದ ನೋಟದ ಎಡಕ್ಕೆ","ಪಕ್ಕದ ನೋಟದ ಬಲಕ್ಕೆ"]',
    'Above the top view',
    'In First Angle Projection (used in India), the front view is placed above the top view. The object is between the observer and the projection plane.',
    'ಮೊದಲ ಕೋನ ಪ್ರಕ್ಷೇಪಣೆಯಲ್ಲಿ (ಭಾರತದಲ್ಲಿ ಬಳಸಲಾಗುತ್ತದೆ), ಮುಂಭಾಗದ ನೋಟವನ್ನು ಮೇಲ್ನೋಟದ ಮೇಲೆ ಇರಿಸಲಾಗುತ್ತದೆ.',
    1);
`;

/** Skill module seed content for employability section */
export const SEED_SKILL_MODULES = [
  {
    type: 'digital_literacy',
    title_en: 'Computer & Internet Basics',
    title_kn: 'ಕಂಪ್ಯೂಟರ್ ಮತ್ತು ಇಂಟರ್ನೆಟ್ ಮೂಲಗಳು',
    lessons: [
      {
        title_en: 'Using a Computer',
        title_kn: 'ಕಂಪ್ಯೂಟರ್ ಬಳಸುವುದು',
        content_en: 'Learn the basics of operating a computer: turning it on/off, using keyboard and mouse, managing files and folders, and understanding operating systems.',
        tips: ['Practice typing daily', 'Learn keyboard shortcuts', 'Organize files in folders'],
      },
      {
        title_en: 'Email & Internet',
        title_kn: 'ಇಮೇಲ್ ಮತ್ತು ಇಂಟರ್ನೆಟ್',
        content_en: 'Learn to write professional emails, browse the internet safely, and use search engines effectively.',
        tips: ['Use professional email address', 'Check emails daily', 'Be careful with links'],
      },
      {
        title_en: 'MS Office Basics',
        title_kn: 'MS ಆಫೀಸ್ ಮೂಲಗಳು',
        content_en: 'Learn basics of Word (documents), Excel (spreadsheets), and PowerPoint (presentations).',
        tips: ['Practice making resumes in Word', 'Learn basic Excel formulas', 'Keep presentations simple'],
      },
    ],
  },
  {
    type: 'communication',
    title_en: 'Speak & Write Better',
    title_kn: 'ಉತ್ತಮವಾಗಿ ಮಾತನಾಡಿ ಮತ್ತು ಬರೆಯಿರಿ',
    lessons: [
      {
        title_en: 'Self Introduction',
        title_kn: 'ಸ್ವಯಂ ಪರಿಚಯ',
        content_en: 'Learn to introduce yourself confidently in interviews and professional settings. Cover your name, education, skills, and career goals.',
        tips: ['Keep it under 2 minutes', 'Mention your strengths', 'Practice in front of mirror'],
      },
      {
        title_en: 'Writing Professional Emails',
        title_kn: 'ವೃತ್ತಿಪರ ಇಮೇಲ್ ಬರೆಯುವುದು',
        content_en: 'Structure an email properly: subject line, greeting, body, and closing. Learn formal language and common phrases.',
        tips: ['Always write clear subject lines', 'Proofread before sending', 'Be concise and polite'],
      },
    ],
  },
  {
    type: 'problem_solving',
    title_en: 'Think Like an Engineer',
    title_kn: 'ಎಂಜಿನಿಯರ್‌ನಂತೆ ಯೋಚಿಸಿ',
    lessons: [
      {
        title_en: 'Logical Thinking',
        title_kn: 'ತಾರ್ಕಿಕ ಚಿಂತನೆ',
        content_en: 'Learn to break down complex problems into smaller parts. Practice pattern recognition and logical reasoning.',
        tips: ['Draw diagrams', 'List all given information', 'Check your answer'],
      },
      {
        title_en: 'Reading Data',
        title_kn: 'ಡೇಟಾ ಓದುವುದು',
        content_en: 'Learn to read graphs, charts, and tables. Understand percentages, averages, and trends in data.',
        tips: ['Always read axis labels', 'Look for trends', 'Compare values carefully'],
      },
    ],
  },
  {
    type: 'interview_skills',
    title_en: 'Get Ready for Interviews',
    title_kn: 'ಸಂದರ್ಶನಗಳಿಗೆ ಸಿದ್ಧರಾಗಿ',
    lessons: [
      {
        title_en: 'Common HR Questions',
        title_kn: 'ಸಾಮಾನ್ಯ HR ಪ್ರಶ್ನೆಗಳು',
        content_en: 'Prepare for: Tell me about yourself, Why should we hire you, What are your strengths/weaknesses, Where do you see yourself in 5 years.',
        tips: ['Be honest', 'Give specific examples', 'Show enthusiasm'],
      },
      {
        title_en: 'Body Language & Confidence',
        title_kn: 'ದೇಹ ಭಾಷೆ ಮತ್ತು ಆತ್ಮವಿಶ್ವಾಸ',
        content_en: 'Make eye contact, sit straight, shake hands firmly, smile naturally. Your body language speaks louder than words.',
        tips: ['Practice with friends', 'Record yourself', 'Breathe and stay calm'],
      },
    ],
  },
];

/** Mock interview questions */
export const MOCK_INTERVIEW_QUESTIONS = {
  hr: [
    { question_en: 'Tell me about yourself.', question_kn: 'ನಿಮ್ಮ ಬಗ್ಗೆ ಹೇಳಿ.', keywords: ['name', 'education', 'skills', 'goal', 'experience'] },
    { question_en: 'Why should we hire you?', question_kn: 'ನಾವು ನಿಮ್ಮನ್ನು ಏಕೆ ನೇಮಿಸಿಕೊಳ್ಳಬೇಕು?', keywords: ['skill', 'value', 'contribute', 'learn', 'team'] },
    { question_en: 'What are your strengths?', question_kn: 'ನಿಮ್ಮ ಸಾಮರ್ಥ್ಯಗಳು ಏನು?', keywords: ['strength', 'good', 'ability', 'skill', 'fast'] },
    { question_en: 'Where do you see yourself in 5 years?', question_kn: '5 ವರ್ಷಗಳಲ್ಲಿ ನಿಮ್ಮನ್ನು ಎಲ್ಲಿ ನೋಡುತ್ತೀರಿ?', keywords: ['grow', 'lead', 'learn', 'career', 'develop'] },
    { question_en: 'Why do you want to work at our company?', question_kn: 'ನೀವು ನಮ್ಮ ಕಂಪನಿಯಲ್ಲಿ ಏಕೆ ಕೆಲಸ ಮಾಡಲು ಬಯಸುತ್ತೀರಿ?', keywords: ['company', 'growth', 'culture', 'value', 'mission'] },
  ],
  technical: [
    { question_en: 'What is an algorithm? Give an example.', question_kn: 'ಕ್ರಮಾವಳಿ ಎಂದರೇನು? ಒಂದು ಉದಾಹರಣೆ ನೀಡಿ.', keywords: ['step', 'procedure', 'solve', 'problem', 'input', 'output'] },
    { question_en: 'Explain the difference between a stack and a queue.', question_kn: 'ಸ್ಟಾಕ್ ಮತ್ತು ಕ್ಯೂ ನಡುವಿನ ವ್ಯತ್ಯಾಸವನ್ನು ವಿವರಿಸಿ.', keywords: ['LIFO', 'FIFO', 'push', 'pop', 'enqueue', 'dequeue'] },
    { question_en: 'What is the difference between RAM and ROM?', question_kn: 'RAM ಮತ್ತು ROM ನಡುವಿನ ವ್ಯತ್ಯಾಸ ಏನು?', keywords: ['volatile', 'permanent', 'memory', 'read', 'write', 'temporary'] },
    { question_en: 'What is normalization in databases?', question_kn: 'ಡೇಟಾಬೇಸ್‌ಗಳಲ್ಲಿ ಸಾಮಾನ್ಯೀಕರಣ ಎಂದರೇನು?', keywords: ['redundancy', 'table', 'normal', 'form', 'dependency', 'data'] },
    { question_en: "Explain Newton's three laws of motion.", question_kn: 'ನ್ಯೂಟನ್ ಮೂರು ಚಲನೆಯ ನಿಯಮಗಳನ್ನು ವಿವರಿಸಿ.', keywords: ['inertia', 'force', 'mass', 'acceleration', 'action', 'reaction'] },
  ],
};
