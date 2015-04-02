begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologMachineCopy
operator|.
name|save
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|CompileException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|BufferingPrologControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|JavaObjectTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologClassLoader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologMachineCopy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SymbolTerm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|LargeObjectException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PushbackReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|ReferenceQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Manages a cache of compiled Prolog rules.  *<p>  * Rules are loaded from the {@code site_path/cache/rules/rules-SHA1.jar}, where  * {@code SHA1} is the SHA1 of the Prolog {@code rules.pl} in a project's  * {@link RefNames#REFS_CONFIG} branch.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|RulesCache
specifier|public
class|class
name|RulesCache
block|{
comment|/** Maximum size of a dynamic Prolog script, in bytes. */
DECL|field|SRC_LIMIT
specifier|private
specifier|static
specifier|final
name|int
name|SRC_LIMIT
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
comment|/** Default size of the internal Prolog database within each interpreter. */
DECL|field|DB_MAX
specifier|private
specifier|static
specifier|final
name|int
name|DB_MAX
init|=
literal|256
decl_stmt|;
DECL|field|PACKAGE_LIST
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|PACKAGE_LIST
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"gerrit"
argument_list|)
decl_stmt|;
DECL|field|machineCache
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|MachineRef
argument_list|>
name|machineCache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|dead
specifier|private
specifier|final
name|ReferenceQueue
argument_list|<
name|PrologMachineCopy
argument_list|>
name|dead
init|=
operator|new
name|ReferenceQueue
argument_list|<>
argument_list|()
decl_stmt|;
DECL|class|MachineRef
specifier|private
specifier|static
specifier|final
class|class
name|MachineRef
extends|extends
name|WeakReference
argument_list|<
name|PrologMachineCopy
argument_list|>
block|{
DECL|field|key
specifier|final
name|ObjectId
name|key
decl_stmt|;
DECL|method|MachineRef (ObjectId key, PrologMachineCopy pcm, ReferenceQueue<PrologMachineCopy> queue)
name|MachineRef
parameter_list|(
name|ObjectId
name|key
parameter_list|,
name|PrologMachineCopy
name|pcm
parameter_list|,
name|ReferenceQueue
argument_list|<
name|PrologMachineCopy
argument_list|>
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|pcm
argument_list|,
name|queue
argument_list|)
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
block|}
DECL|field|enableProjectRules
specifier|private
specifier|final
name|boolean
name|enableProjectRules
decl_stmt|;
DECL|field|cacheDir
specifier|private
specifier|final
name|Path
name|cacheDir
decl_stmt|;
DECL|field|rulesDir
specifier|private
specifier|final
name|Path
name|rulesDir
decl_stmt|;
DECL|field|gitMgr
specifier|private
specifier|final
name|GitRepositoryManager
name|gitMgr
decl_stmt|;
DECL|field|predicateProviders
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PredicateProvider
argument_list|>
name|predicateProviders
decl_stmt|;
DECL|field|systemLoader
specifier|private
specifier|final
name|ClassLoader
name|systemLoader
decl_stmt|;
DECL|field|defaultMachine
specifier|private
specifier|final
name|PrologMachineCopy
name|defaultMachine
decl_stmt|;
annotation|@
name|Inject
DECL|method|RulesCache (@erritServerConfig Config config, SitePaths site, GitRepositoryManager gm, DynamicSet<PredicateProvider> predicateProviders)
specifier|protected
name|RulesCache
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|GitRepositoryManager
name|gm
parameter_list|,
name|DynamicSet
argument_list|<
name|PredicateProvider
argument_list|>
name|predicateProviders
parameter_list|)
block|{
name|enableProjectRules
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"rules"
argument_list|,
literal|null
argument_list|,
literal|"enable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cacheDir
operator|=
name|site
operator|.
name|resolve
argument_list|(
name|config
operator|.
name|getString
argument_list|(
literal|"cache"
argument_list|,
literal|null
argument_list|,
literal|"directory"
argument_list|)
argument_list|)
expr_stmt|;
name|rulesDir
operator|=
name|cacheDir
operator|!=
literal|null
condition|?
name|cacheDir
operator|.
name|resolve
argument_list|(
literal|"rules"
argument_list|)
else|:
literal|null
expr_stmt|;
name|gitMgr
operator|=
name|gm
expr_stmt|;
name|this
operator|.
name|predicateProviders
operator|=
name|predicateProviders
expr_stmt|;
name|systemLoader
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
name|defaultMachine
operator|=
name|save
argument_list|(
name|newEmptyMachine
argument_list|(
name|systemLoader
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isProjectRulesEnabled ()
specifier|public
name|boolean
name|isProjectRulesEnabled
parameter_list|()
block|{
return|return
name|enableProjectRules
return|;
block|}
comment|/**    * Locate a cached Prolog machine state, or create one if not available.    *    * @return a Prolog machine, after loading the specified rules.    * @throws CompileException the machine cannot be created.    */
DECL|method|loadMachine ( Project.NameKey project, ObjectId rulesId)
specifier|public
specifier|synchronized
name|PrologMachineCopy
name|loadMachine
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ObjectId
name|rulesId
parameter_list|)
throws|throws
name|CompileException
block|{
if|if
condition|(
operator|!
name|enableProjectRules
operator|||
name|project
operator|==
literal|null
operator|||
name|rulesId
operator|==
literal|null
condition|)
block|{
return|return
name|defaultMachine
return|;
block|}
name|Reference
argument_list|<
name|?
extends|extends
name|PrologMachineCopy
argument_list|>
name|ref
init|=
name|machineCache
operator|.
name|get
argument_list|(
name|rulesId
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|PrologMachineCopy
name|pmc
init|=
name|ref
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|pmc
operator|!=
literal|null
condition|)
block|{
return|return
name|pmc
return|;
block|}
name|machineCache
operator|.
name|remove
argument_list|(
name|rulesId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|enqueue
argument_list|()
expr_stmt|;
block|}
name|gc
argument_list|()
expr_stmt|;
name|PrologMachineCopy
name|pcm
init|=
name|createMachine
argument_list|(
name|project
argument_list|,
name|rulesId
argument_list|)
decl_stmt|;
name|MachineRef
name|newRef
init|=
operator|new
name|MachineRef
argument_list|(
name|rulesId
argument_list|,
name|pcm
argument_list|,
name|dead
argument_list|)
decl_stmt|;
name|machineCache
operator|.
name|put
argument_list|(
name|rulesId
argument_list|,
name|newRef
argument_list|)
expr_stmt|;
return|return
name|pcm
return|;
block|}
DECL|method|loadMachine (String name, Reader in)
specifier|public
name|PrologMachineCopy
name|loadMachine
parameter_list|(
name|String
name|name
parameter_list|,
name|Reader
name|in
parameter_list|)
throws|throws
name|CompileException
block|{
name|PrologMachineCopy
name|pmc
init|=
name|consultRules
argument_list|(
name|name
argument_list|,
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|pmc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot consult rules from the stream "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|pmc
return|;
block|}
DECL|method|gc ()
specifier|private
name|void
name|gc
parameter_list|()
block|{
name|Reference
argument_list|<
name|?
argument_list|>
name|ref
decl_stmt|;
while|while
condition|(
operator|(
name|ref
operator|=
name|dead
operator|.
name|poll
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|ObjectId
name|key
init|=
operator|(
operator|(
name|MachineRef
operator|)
name|ref
operator|)
operator|.
name|key
decl_stmt|;
if|if
condition|(
name|machineCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
name|ref
condition|)
block|{
name|machineCache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createMachine (Project.NameKey project, ObjectId rulesId)
specifier|private
name|PrologMachineCopy
name|createMachine
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ObjectId
name|rulesId
parameter_list|)
throws|throws
name|CompileException
block|{
comment|// If the rules are available as a complied JAR on local disk, prefer
comment|// that over dynamic consult as the bytecode will be faster.
comment|//
if|if
condition|(
name|rulesDir
operator|!=
literal|null
condition|)
block|{
name|Path
name|jarPath
init|=
name|rulesDir
operator|.
name|resolve
argument_list|(
literal|"rules-"
operator|+
name|rulesId
operator|.
name|getName
argument_list|()
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|jarPath
argument_list|)
condition|)
block|{
name|URL
index|[]
name|cp
init|=
operator|new
name|URL
index|[]
block|{
name|toURL
argument_list|(
name|jarPath
argument_list|)
block|}
decl_stmt|;
return|return
name|save
argument_list|(
name|newEmptyMachine
argument_list|(
operator|new
name|URLClassLoader
argument_list|(
name|cp
argument_list|,
name|systemLoader
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|// Dynamically consult the rules into the machine's internal database.
comment|//
name|String
name|rules
init|=
name|read
argument_list|(
name|project
argument_list|,
name|rulesId
argument_list|)
decl_stmt|;
name|PrologMachineCopy
name|pmc
init|=
name|consultRules
argument_list|(
literal|"rules.pl"
argument_list|,
operator|new
name|StringReader
argument_list|(
name|rules
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|pmc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot consult rules of "
operator|+
name|project
argument_list|)
throw|;
block|}
return|return
name|pmc
return|;
block|}
DECL|method|consultRules (String name, Reader rules)
specifier|private
name|PrologMachineCopy
name|consultRules
parameter_list|(
name|String
name|name
parameter_list|,
name|Reader
name|rules
parameter_list|)
throws|throws
name|CompileException
block|{
name|BufferingPrologControl
name|ctl
init|=
name|newEmptyMachine
argument_list|(
name|systemLoader
argument_list|)
decl_stmt|;
name|PushbackReader
name|in
init|=
operator|new
name|PushbackReader
argument_list|(
name|rules
argument_list|,
name|Prolog
operator|.
name|PUSHBACK_SIZE
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|ctl
operator|.
name|execute
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"consult_stream"
argument_list|,
name|SymbolTerm
operator|.
name|intern
argument_list|(
name|name
argument_list|)
argument_list|,
operator|new
name|JavaObjectTerm
argument_list|(
name|in
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Error while consulting rules from "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|save
argument_list|(
name|ctl
argument_list|)
return|;
block|}
DECL|method|read (Project.NameKey project, ObjectId rulesId)
specifier|private
name|String
name|read
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ObjectId
name|rulesId
parameter_list|)
throws|throws
name|CompileException
block|{
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot open repository "
operator|+
name|project
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot open repository "
operator|+
name|project
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|ObjectLoader
name|ldr
init|=
name|git
operator|.
name|open
argument_list|(
name|rulesId
argument_list|,
name|Constants
operator|.
name|OBJ_BLOB
argument_list|)
decl_stmt|;
name|byte
index|[]
name|raw
init|=
name|ldr
operator|.
name|getCachedBytes
argument_list|(
name|SRC_LIMIT
argument_list|)
decl_stmt|;
return|return
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|raw
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LargeObjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"rules of "
operator|+
name|project
operator|+
literal|" are too large"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot load rules of "
operator|+
name|project
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot load rules of "
operator|+
name|project
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|newEmptyMachine (ClassLoader cl)
specifier|private
name|BufferingPrologControl
name|newEmptyMachine
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
name|BufferingPrologControl
name|ctl
init|=
operator|new
name|BufferingPrologControl
argument_list|()
decl_stmt|;
name|ctl
operator|.
name|setMaxDatabaseSize
argument_list|(
name|DB_MAX
argument_list|)
expr_stmt|;
name|ctl
operator|.
name|setPrologClassLoader
argument_list|(
operator|new
name|PrologClassLoader
argument_list|(
operator|new
name|PredicateClassLoader
argument_list|(
name|predicateProviders
argument_list|,
name|cl
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ctl
operator|.
name|setEnabled
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|Prolog
operator|.
name|Feature
operator|.
name|class
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|packages
operator|.
name|addAll
argument_list|(
name|PACKAGE_LIST
argument_list|)
expr_stmt|;
for|for
control|(
name|PredicateProvider
name|predicateProvider
range|:
name|predicateProviders
control|)
block|{
name|packages
operator|.
name|addAll
argument_list|(
name|predicateProvider
operator|.
name|getPackages
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Bootstrap the interpreter and ensure there is clean state.
name|ctl
operator|.
name|initialize
argument_list|(
name|packages
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|packages
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ctl
return|;
block|}
DECL|method|toURL (Path jarPath)
specifier|private
specifier|static
name|URL
name|toURL
parameter_list|(
name|Path
name|jarPath
parameter_list|)
throws|throws
name|CompileException
block|{
try|try
block|{
return|return
name|jarPath
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot create URL for "
operator|+
name|jarPath
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

