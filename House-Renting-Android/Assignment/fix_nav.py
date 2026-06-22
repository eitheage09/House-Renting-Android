import os, re
src_dir = r'Assignment/app/src/main/java/com/example/assignment/ui'
for root, dirs, files in os.walk(src_dir):
    for f in files:
        if not f.endswith('.java'): continue
        path = os.path.join(root, f)
        with open(path, 'r', encoding='utf-8') as fh: lines = fh.readlines()
        old = list(lines)
        mod = False
        # Step 1: replace import
        for i, line in enumerate(lines):
            if 'import androidx.navigation.fragment.findNavController' in line:
                lines[i] = 'import androidx.navigation.NavController;\n'
                lines.insert(i+1, 'import androidx.navigation.fragment.NavHostFragment;\n')
                mod = True; break
        # Step 2: add nav field after binding field
        pat = re.compile(r'^\s+private\s+\w+Binding\s+\w+binding\s*;')
        for i, line in enumerate(lines):
            if pat.match(line) and not any('NavController nav;' in l for l in lines):
                lines.insert(i+1, '    private NavController nav;\n'); mod = True; break
        # Step 3: add nav init after binding.inflate
        ipat = re.compile(r'^\s+binding\s*=\s*\w+Binding\.inflate\(inflater,\s*container,\s*false\)\s*;')
        for i, line in enumerate(lines):
            if ipat.match(line):
                lines.insert(i+1, '        nav = NavHostFragment.findNavController(this);\n'); mod = True; break
        if mod:
            with open(path, 'w', encoding='utf-8') as fh: fh.writelines(lines)
            print(f'FIXED: {f}')
        else:
            print(f'OK: {f}')
