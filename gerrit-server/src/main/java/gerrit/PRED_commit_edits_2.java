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
DECL|package|gerrit
package|package
name|gerrit
package|;
end_package

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
name|PatchSetInfo
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
name|rules
operator|.
name|PrologEnvironment
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
name|rules
operator|.
name|StoredValues
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
name|patch
operator|.
name|PatchList
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
name|patch
operator|.
name|PatchListEntry
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
name|patch
operator|.
name|Text
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
name|IllegalTypeException
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
name|JavaException
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
name|Operation
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
name|PInstantiationException
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
name|Predicate
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
name|PrologException
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
name|SystemException
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
name|Term
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
name|diff
operator|.
name|Edit
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
name|CorruptObjectException
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
name|IncorrectObjectTypeException
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
name|MissingObjectException
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
name|ObjectReader
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
name|RevTree
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
name|treewalk
operator|.
name|TreeWalk
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Returns true if any of the files that match FileNameRegex have edited lines  * that match EditRegex  *  *<pre>  *   'commit_edits'(+FileNameRegex, +EditRegex)  *</pre>  */
end_comment

begin_class
DECL|class|PRED_commit_edits_2
specifier|public
class|class
name|PRED_commit_edits_2
extends|extends
name|Predicate
operator|.
name|P2
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|PRED_commit_edits_2 (Term a1, Term a2, Operation n)
specifier|public
name|PRED_commit_edits_2
parameter_list|(
name|Term
name|a1
parameter_list|,
name|Term
name|a2
parameter_list|,
name|Operation
name|n
parameter_list|)
block|{
name|arg1
operator|=
name|a1
expr_stmt|;
name|arg2
operator|=
name|a2
expr_stmt|;
name|cont
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exec (Prolog engine)
specifier|public
name|Operation
name|exec
parameter_list|(
name|Prolog
name|engine
parameter_list|)
throws|throws
name|PrologException
block|{
name|engine
operator|.
name|setB0
argument_list|()
expr_stmt|;
name|Term
name|a1
init|=
name|arg1
operator|.
name|dereference
argument_list|()
decl_stmt|;
name|Term
name|a2
init|=
name|arg2
operator|.
name|dereference
argument_list|()
decl_stmt|;
name|Pattern
name|fileRegex
init|=
name|getRegexParameter
argument_list|(
name|a1
argument_list|)
decl_stmt|;
name|Pattern
name|editRegex
init|=
name|getRegexParameter
argument_list|(
name|a2
argument_list|)
decl_stmt|;
name|PrologEnvironment
name|env
init|=
operator|(
name|PrologEnvironment
operator|)
name|engine
operator|.
name|control
decl_stmt|;
name|PatchSetInfo
name|psInfo
init|=
name|StoredValues
operator|.
name|PATCH_SET_INFO
operator|.
name|get
argument_list|(
name|engine
argument_list|)
decl_stmt|;
name|PatchList
name|pl
init|=
name|StoredValues
operator|.
name|PATCH_LIST
operator|.
name|get
argument_list|(
name|engine
argument_list|)
decl_stmt|;
name|Repository
name|repo
init|=
name|StoredValues
operator|.
name|REPOSITORY
operator|.
name|get
argument_list|(
name|engine
argument_list|)
decl_stmt|;
specifier|final
name|ObjectReader
name|reader
init|=
name|repo
operator|.
name|newObjectReader
argument_list|()
decl_stmt|;
specifier|final
name|RevTree
name|aTree
decl_stmt|;
specifier|final
name|RevTree
name|bTree
decl_stmt|;
try|try
block|{
specifier|final
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
decl_stmt|;
specifier|final
name|RevCommit
name|bCommit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|pl
operator|.
name|getNewId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pl
operator|.
name|getOldId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|aTree
operator|=
name|rw
operator|.
name|parseTree
argument_list|(
name|pl
operator|.
name|getOldId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Octopus merge with unknown automatic merge result, since the
comment|// web UI returns no files to match against, just fail.
return|return
name|engine
operator|.
name|fail
argument_list|()
return|;
block|}
name|bTree
operator|=
name|bCommit
operator|.
name|getTree
argument_list|()
expr_stmt|;
for|for
control|(
name|PatchListEntry
name|entry
range|:
name|pl
operator|.
name|getPatches
argument_list|()
control|)
block|{
name|String
name|newName
init|=
name|entry
operator|.
name|getNewName
argument_list|()
decl_stmt|;
name|String
name|oldName
init|=
name|entry
operator|.
name|getOldName
argument_list|()
decl_stmt|;
if|if
condition|(
name|newName
operator|.
name|equals
argument_list|(
literal|"/COMMIT_MSG"
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|fileRegex
operator|.
name|matcher
argument_list|(
name|newName
argument_list|)
operator|.
name|find
argument_list|()
operator|||
operator|(
name|oldName
operator|!=
literal|null
operator|&&
name|fileRegex
operator|.
name|matcher
argument_list|(
name|oldName
argument_list|)
operator|.
name|find
argument_list|()
operator|)
condition|)
block|{
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
init|=
name|entry
operator|.
name|getEdits
argument_list|()
decl_stmt|;
if|if
condition|(
name|edits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Text
name|tA
decl_stmt|;
if|if
condition|(
name|oldName
operator|!=
literal|null
condition|)
block|{
name|tA
operator|=
name|load
argument_list|(
name|aTree
argument_list|,
name|oldName
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tA
operator|=
name|load
argument_list|(
name|aTree
argument_list|,
name|newName
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
name|Text
name|tB
init|=
name|load
argument_list|(
name|bTree
argument_list|,
name|newName
argument_list|,
name|reader
argument_list|)
decl_stmt|;
for|for
control|(
name|Edit
name|edit
range|:
name|edits
control|)
block|{
if|if
condition|(
name|tA
operator|!=
name|Text
operator|.
name|EMPTY
condition|)
block|{
name|String
name|aDiff
init|=
name|tA
operator|.
name|getString
argument_list|(
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|editRegex
operator|.
name|matcher
argument_list|(
name|aDiff
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
name|cont
return|;
block|}
block|}
if|if
condition|(
name|tB
operator|!=
name|Text
operator|.
name|EMPTY
condition|)
block|{
name|String
name|bDiff
init|=
name|tB
operator|.
name|getString
argument_list|(
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|editRegex
operator|.
name|matcher
argument_list|(
name|bDiff
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
name|cont
return|;
block|}
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|JavaException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|,
name|err
argument_list|)
throw|;
block|}
finally|finally
block|{
name|reader
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
return|return
name|engine
operator|.
name|fail
argument_list|()
return|;
block|}
DECL|method|getRegexParameter (Term term)
specifier|private
name|Pattern
name|getRegexParameter
parameter_list|(
name|Term
name|term
parameter_list|)
block|{
if|if
condition|(
name|term
operator|.
name|isVariable
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PInstantiationException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|term
operator|.
name|isSymbol
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalTypeException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|,
literal|"symbol"
argument_list|,
name|term
argument_list|)
throw|;
block|}
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|term
operator|.
name|name
argument_list|()
argument_list|,
name|Pattern
operator|.
name|MULTILINE
argument_list|)
return|;
block|}
DECL|method|load (final ObjectId tree, final String path, final ObjectReader reader)
specifier|private
name|Text
name|load
parameter_list|(
specifier|final
name|ObjectId
name|tree
parameter_list|,
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|ObjectReader
name|reader
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|CorruptObjectException
throws|,
name|IOException
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|Text
operator|.
name|EMPTY
return|;
block|}
specifier|final
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|reader
argument_list|,
name|path
argument_list|,
name|tree
argument_list|)
decl_stmt|;
if|if
condition|(
name|tw
operator|==
literal|null
condition|)
block|{
return|return
name|Text
operator|.
name|EMPTY
return|;
block|}
if|if
condition|(
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectType
argument_list|()
operator|!=
name|Constants
operator|.
name|OBJ_BLOB
condition|)
block|{
return|return
name|Text
operator|.
name|EMPTY
return|;
block|}
return|return
operator|new
name|Text
argument_list|(
name|reader
operator|.
name|open
argument_list|(
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|,
name|Constants
operator|.
name|OBJ_BLOB
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

