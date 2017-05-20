import glob, os
import json

params = {
	'archive_dir': 'archive', # Папка с архивом
	'data_files': './data/js/tweets/*.js',
	'output_file': '../output.txt' # Название файла с твитами
}

def load_data(files):
	items = []
	files = glob.glob(files)
	for file in files:
		with open(file) as f:
			d = f.readlines()[1:]  
			d = "".join(d)
			j = json.loads(d)
			for tweet in j:
				text = tweet["text"]
				items.append(text)
	return items

def write_json(data, output_file):
	with open(output_file, 'w', encoding='utf-8') as f:
		for i in reversed(data):
			f.write(i + '\n')

def main():
	os.chdir(params['archive_dir']) # Переходим в папку с архивом
	d = load_data(params['data_files']) # Получаем данные из архива
	write_json(d, params['output_file']) # Записываем данные в файл

if __name__ == "__main__":
	main()
