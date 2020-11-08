OLD_PLUGIN_PACKAGE_NAME="pluginexample"
OLD_PLUGIN_CLASS_PREFIX="PluginExample"
OLD_PLUGIN_NAME="plugin-example"

NEW_PLUGIN_PACKAGE_NAME="$NEW_PLUGIN_PACKAGE_NAME"
NEW_PLUGIN_CLASS_PREFIX="$NEW_PLUGIN_CLASS_PREFIX"
NEW_PLUGIN_NAME="$NEW_PLUGIN_NAME"

# check arguments
if [ -z "$NEW_PLUGIN_PACKAGE_NAME" ]
then
	echo "missing argument: NEW_PLUGIN_PACKAGE_NAME"
	exit 1
fi
if [ -z "$NEW_PLUGIN_CLASS_PREFIX" ]
then
	echo "missing argument: NEW_PLUGIN_CLASS_PREFIX"
	exit 1
fi
if [ -z "$NEW_PLUGIN_NAME" ]
then
	echo "missing argument: NEW_PLUGIN_NAME"
	exit 1
fi

# check directory
foo=`ls | grep $NEW_PLUGIN_NAME`
if [ ! -z "$foo" ]
then
	echo "$NEW_PLUGIN_NAME already exists"
	exit 1
fi
foo=`ls | grep $OLD_PLUGIN_NAME`
if [ -z "$foo" ]
then
	echo "$OLD_PLUGIN_NAME does not exist"
	exit 1
fi

# run
set -e

# plugin dir
cp -r "$OLD_PLUGIN_NAME" "$NEW_PLUGIN_NAME"
cd "$NEW_PLUGIN_NAME"

# reset project
rm -rf .gradle/
rm -rf build/
rm -rf .idea/
mv plugin/plugin.json ./backup-plugin.json
rm -rf plugin
mkdir plugin
mv ./backup-plugin.json plugin/plugin.json
mkdir plugin/code
mkdir plugin/$NEW_PLUGIN_NAME

# code directory
mv src/main/java/net/cassite/desktop/chara/plugin/$OLD_PLUGIN_PACKAGE_NAME \
   src/main/java/net/cassite/desktop/chara/plugin/$NEW_PLUGIN_PACKAGE_NAME

mv src/main/java/net/cassite/desktop/chara/plugin/$NEW_PLUGIN_PACKAGE_NAME/"$OLD_PLUGIN_CLASS_PREFIX"Plugin.java \
   src/main/java/net/cassite/desktop/chara/plugin/$NEW_PLUGIN_PACKAGE_NAME/"$NEW_PLUGIN_CLASS_PREFIX"Plugin.java

mv src/main/java/run/plugin/$OLD_PLUGIN_PACKAGE_NAME \
   src/main/java/run/plugin/$NEW_PLUGIN_PACKAGE_NAME

# replace content
files_to_replace="Makefile.template
./.gitignore
./build-plugin.sh
./build.gradle
./settings.gradle
./plugin/plugin.json
./src/main/java/module-info.java
./src/main/java/net/cassite/desktop/chara/plugin/$NEW_PLUGIN_PACKAGE_NAME/"$NEW_PLUGIN_CLASS_PREFIX"Plugin.java
./src/main/java/run/plugin/$NEW_PLUGIN_PACKAGE_NAME/Run.java"

for file in $files_to_replace
do
	sed -i -E "s/$OLD_PLUGIN_PACKAGE_NAME/$NEW_PLUGIN_PACKAGE_NAME/g" $file
	sed -i -E "s/$OLD_PLUGIN_CLASS_PREFIX/$NEW_PLUGIN_CLASS_PREFIX/g" $file
	sed -i -E "s/$OLD_PLUGIN_NAME/$NEW_PLUGIN_NAME/g" $file
	rm -f "$file"-E
done

# final check
set +e
foo=`grep -rni "$OLD_PLUGIN_PACKAGE_NAME" .`
if [ ! -z "$foo" ]
then
	echo "[WARNING] $OLD_PLUGIN_PACKAGE_NAME still exists, something went wrong"
	echo "$foo"
fi
foo=`grep -rni "$OLD_PLUGIN_CLASS_PREFIX" .`
if [ ! -z "$foo" ]
then
	echo "[WARNING] $OLD_PLUGIN_CLASS_PREFIX still exists, something went wrong"
	echo "$foo"
fi
foo=`grep -rni "$OLD_PLUGIN_NAME" .`
if [ ! -z "$foo" ]
then
	echo "[WARNING] $OLD_PLUGIN_NAME still exists, something went wrong"
	echo "$foo"
fi

# done
echo "done"
exit 0
