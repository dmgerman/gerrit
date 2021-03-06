begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|base
operator|.
name|Splitter
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
name|ImmutableMap
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
name|restapi
operator|.
name|AuthException
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
name|change
operator|.
name|ArchiveFormat
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectState
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
name|restapi
operator|.
name|change
operator|.
name|AllowedFormats
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
name|restapi
operator|.
name|project
operator|.
name|CommitsCollection
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
name|sshd
operator|.
name|AbstractGitCommand
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|ArchiveCommand
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
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
name|transport
operator|.
name|PacketLineIn
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
name|transport
operator|.
name|PacketLineOut
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
name|transport
operator|.
name|SideBandOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|ParserProperties
import|;
end_import

begin_comment
comment|/** Allows getting archives for Git repositories over SSH using the Git upload-archive protocol. */
end_comment

begin_class
DECL|class|UploadArchive
specifier|public
class|class
name|UploadArchive
extends|extends
name|AbstractGitCommand
block|{
comment|/**    * Options for parsing Git commands.    *    *<p>These options are not passed on command line, but received through input stream in pkt-line    * format.    */
DECL|class|Options
specifier|static
class|class
name|Options
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
block|{
literal|"--format"
block|}
argument_list|,
name|usage
operator|=
literal|"Format of the"
operator|+
literal|" resulting archive: tar or zip... If this option is not given, and"
operator|+
literal|" the output file is specified, the format is inferred from the"
operator|+
literal|" filename if possible (e.g. writing to \"foo.zip\" makes the output"
operator|+
literal|" to be in the zip format). Otherwise the output format is tar."
argument_list|)
DECL|field|format
specifier|private
name|String
name|format
init|=
literal|"tar"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--prefix"
argument_list|,
name|usage
operator|=
literal|"Prepend<prefix>/ to each filename in the archive."
argument_list|)
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-0"
argument_list|,
name|usage
operator|=
literal|"Store the files instead of deflating them."
argument_list|)
DECL|field|level0
specifier|private
name|boolean
name|level0
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-1"
argument_list|)
DECL|field|level1
specifier|private
name|boolean
name|level1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-2"
argument_list|)
DECL|field|level2
specifier|private
name|boolean
name|level2
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-3"
argument_list|)
DECL|field|level3
specifier|private
name|boolean
name|level3
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-4"
argument_list|)
DECL|field|level4
specifier|private
name|boolean
name|level4
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-5"
argument_list|)
DECL|field|level5
specifier|private
name|boolean
name|level5
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-6"
argument_list|)
DECL|field|level6
specifier|private
name|boolean
name|level6
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-7"
argument_list|)
DECL|field|level7
specifier|private
name|boolean
name|level7
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-8"
argument_list|)
DECL|field|level8
specifier|private
name|boolean
name|level8
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-9"
argument_list|,
name|usage
operator|=
literal|"Highest and slowest compression level. You "
operator|+
literal|"can specify any number from 1 to 9 to adjust compression speed and "
operator|+
literal|"ratio."
argument_list|)
DECL|field|level9
specifier|private
name|boolean
name|level9
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"The tree or commit to produce an archive for."
argument_list|)
DECL|field|treeIsh
specifier|private
name|String
name|treeIsh
init|=
literal|"master"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"Without an optional path parameter, all files and subdirectories of "
operator|+
literal|"the current working directory are included in the archive. If one "
operator|+
literal|"or more paths are specified, only these are included."
argument_list|)
DECL|field|path
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|path
decl_stmt|;
block|}
DECL|field|permissionBackend
annotation|@
name|Inject
specifier|private
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|commits
annotation|@
name|Inject
specifier|private
name|CommitsCollection
name|commits
decl_stmt|;
DECL|field|allowedFormats
annotation|@
name|Inject
specifier|private
name|AllowedFormats
name|allowedFormats
decl_stmt|;
DECL|field|projectCache
annotation|@
name|Inject
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|options
specifier|private
name|Options
name|options
init|=
operator|new
name|Options
argument_list|()
decl_stmt|;
comment|/**    * Read and parse arguments from input stream. This method gets the arguments from input stream,    * in Pkt-line format, then parses them to fill the options object.    */
DECL|method|readArguments ()
specifier|protected
name|void
name|readArguments
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
name|String
name|argCmd
init|=
literal|"argument "
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Read arguments in Pkt-Line format
name|PacketLineIn
name|packetIn
init|=
operator|new
name|PacketLineIn
argument_list|(
name|in
argument_list|)
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|String
name|s
init|=
name|packetIn
operator|.
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
name|PacketLineIn
operator|.
name|isEnd
argument_list|(
name|s
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
operator|!
name|s
operator|.
name|startsWith
argument_list|(
name|argCmd
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: 'argument' token or flush expected, got "
operator|+
name|s
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|p
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|split
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|argCmd
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
control|)
block|{
name|args
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
comment|// Parse them into the 'options' field
name|CmdLineParser
name|parser
init|=
operator|new
name|CmdLineParser
argument_list|(
name|options
argument_list|,
name|ParserProperties
operator|.
name|defaults
argument_list|()
operator|.
name|withAtSyntax
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parseArgument
argument_list|(
name|args
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|path
operator|==
literal|null
operator|||
name|Arrays
operator|.
name|asList
argument_list|(
literal|"."
argument_list|)
operator|.
name|equals
argument_list|(
name|options
operator|.
name|path
argument_list|)
condition|)
block|{
name|options
operator|.
name|path
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CmdLineException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|2
argument_list|,
literal|"fatal: unable to parse arguments, "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|Failure
block|{
name|PacketLineOut
name|packetOut
init|=
operator|new
name|PacketLineOut
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|packetOut
operator|.
name|setFlushOnEnd
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|packetOut
operator|.
name|writeString
argument_list|(
literal|"ACK"
argument_list|)
expr_stmt|;
name|packetOut
operator|.
name|end
argument_list|()
expr_stmt|;
try|try
block|{
comment|// Parse Git arguments
name|readArguments
argument_list|()
expr_stmt|;
name|ArchiveFormat
name|f
init|=
name|allowedFormats
operator|.
name|getExtensions
argument_list|()
operator|.
name|get
argument_list|(
literal|"."
operator|+
name|options
operator|.
name|format
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|3
argument_list|,
literal|"fatal: upload-archive not permitted for format "
operator|+
name|options
operator|.
name|format
argument_list|)
throw|;
block|}
comment|// Find out the object to get from the specified reference and paths
name|ObjectId
name|treeId
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|options
operator|.
name|treeIsh
argument_list|)
decl_stmt|;
if|if
condition|(
name|treeId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|4
argument_list|,
literal|"fatal: reference not found: "
operator|+
name|options
operator|.
name|treeIsh
argument_list|)
throw|;
block|}
comment|// Verify the user has permissions to read the specified tree.
if|if
condition|(
operator|!
name|canRead
argument_list|(
name|treeId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|5
argument_list|,
literal|"fatal: no permission to read tree"
operator|+
name|options
operator|.
name|treeIsh
argument_list|)
throw|;
block|}
comment|// The archive is sent in DATA sideband channel
try|try
init|(
name|SideBandOutputStream
name|sidebandOut
init|=
operator|new
name|SideBandOutputStream
argument_list|(
name|SideBandOutputStream
operator|.
name|CH_DATA
argument_list|,
name|SideBandOutputStream
operator|.
name|MAX_BUF
argument_list|,
name|out
argument_list|)
init|)
block|{
operator|new
name|ArchiveCommand
argument_list|(
name|repo
argument_list|)
operator|.
name|setFormat
argument_list|(
name|f
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|setFormatOptions
argument_list|(
name|getFormatOptions
argument_list|(
name|f
argument_list|)
argument_list|)
operator|.
name|setTree
argument_list|(
name|treeId
argument_list|)
operator|.
name|setPaths
argument_list|(
name|options
operator|.
name|path
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|setPrefix
argument_list|(
name|options
operator|.
name|prefix
argument_list|)
operator|.
name|setOutputStream
argument_list|(
name|sidebandOut
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|sidebandOut
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GitAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|7
argument_list|,
literal|"fatal: git api exception, "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Report the error in ERROR sideband channel. Catch Throwable too so we can also catch
comment|// NoClassDefFound.
try|try
init|(
name|SideBandOutputStream
name|sidebandError
init|=
operator|new
name|SideBandOutputStream
argument_list|(
name|SideBandOutputStream
operator|.
name|CH_ERROR
argument_list|,
name|SideBandOutputStream
operator|.
name|MAX_BUF
argument_list|,
name|out
argument_list|)
init|)
block|{
name|sidebandError
operator|.
name|write
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|sidebandError
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
throw|throw
name|t
throw|;
block|}
finally|finally
block|{
comment|// In any case, cleanly close the packetOut channel
name|packetOut
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getFormatOptions (ArchiveFormat f)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getFormatOptions
parameter_list|(
name|ArchiveFormat
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|==
name|ArchiveFormat
operator|.
name|ZIP
condition|)
block|{
name|int
name|value
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|options
operator|.
name|level0
argument_list|,
name|options
operator|.
name|level1
argument_list|,
name|options
operator|.
name|level2
argument_list|,
name|options
operator|.
name|level3
argument_list|,
name|options
operator|.
name|level4
argument_list|,
name|options
operator|.
name|level5
argument_list|,
name|options
operator|.
name|level6
argument_list|,
name|options
operator|.
name|level7
argument_list|,
name|options
operator|.
name|level8
argument_list|,
name|options
operator|.
name|level9
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|>=
literal|0
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"level"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
DECL|method|canRead (ObjectId revId)
specifier|private
name|boolean
name|canRead
parameter_list|(
name|ObjectId
name|revId
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|requireNonNull
argument_list|(
name|projectState
argument_list|,
parameter_list|()
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"Failed to load project %s"
argument_list|,
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|projectState
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectName
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|READ
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
comment|// Check reachability of the specific revision.
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|revId
argument_list|)
decl_stmt|;
return|return
name|commits
operator|.
name|canRead
argument_list|(
name|projectState
argument_list|,
name|repo
argument_list|,
name|commit
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

