begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|lib
operator|.
name|AbbreviatedObjectId
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
name|AnyObjectId
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
name|ObjectInserter
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
name|transport
operator|.
name|PackParser
import|;
end_import

begin_class
DECL|class|InMemoryInserter
specifier|public
class|class
name|InMemoryInserter
extends|extends
name|ObjectInserter
block|{
DECL|field|reader
specifier|private
specifier|final
name|ObjectReader
name|reader
decl_stmt|;
DECL|field|inserted
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|InsertedObject
argument_list|>
name|inserted
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|closeReader
specifier|private
specifier|final
name|boolean
name|closeReader
decl_stmt|;
DECL|method|InMemoryInserter (ObjectReader reader)
specifier|public
name|InMemoryInserter
parameter_list|(
name|ObjectReader
name|reader
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|checkNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|closeReader
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|InMemoryInserter (Repository repo)
specifier|public
name|InMemoryInserter
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|repo
operator|.
name|newObjectReader
argument_list|()
expr_stmt|;
name|closeReader
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|insert (int type, long length, InputStream in)
specifier|public
name|ObjectId
name|insert
parameter_list|(
name|int
name|type
parameter_list|,
name|long
name|length
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|insert
argument_list|(
name|InsertedObject
operator|.
name|create
argument_list|(
name|type
argument_list|,
name|in
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|insert (int type, byte[] data)
specifier|public
name|ObjectId
name|insert
parameter_list|(
name|int
name|type
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|insert
argument_list|(
name|type
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|insert (int type, byte[] data, int off, int len)
specifier|public
name|ObjectId
name|insert
parameter_list|(
name|int
name|type
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
return|return
name|insert
argument_list|(
name|InsertedObject
operator|.
name|create
argument_list|(
name|type
argument_list|,
name|data
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
argument_list|)
return|;
block|}
DECL|method|insert (InsertedObject obj)
specifier|public
name|ObjectId
name|insert
parameter_list|(
name|InsertedObject
name|obj
parameter_list|)
block|{
name|inserted
operator|.
name|put
argument_list|(
name|obj
operator|.
name|id
argument_list|()
argument_list|,
name|obj
argument_list|)
expr_stmt|;
return|return
name|obj
operator|.
name|id
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|newPackParser (InputStream in)
specifier|public
name|PackParser
name|newPackParser
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|newReader ()
specifier|public
name|ObjectReader
name|newReader
parameter_list|()
block|{
return|return
operator|new
name|Reader
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{
comment|// Do nothing; objects are not written to the repo.
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|closeReader
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getInsertedObjects ()
specifier|public
name|ImmutableList
argument_list|<
name|InsertedObject
argument_list|>
name|getInsertedObjects
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|inserted
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|inserted
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|class|Reader
specifier|private
class|class
name|Reader
extends|extends
name|ObjectReader
block|{
annotation|@
name|Override
DECL|method|newReader ()
specifier|public
name|ObjectReader
name|newReader
parameter_list|()
block|{
return|return
operator|new
name|Reader
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resolve (AbbreviatedObjectId id)
specifier|public
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|resolve
parameter_list|(
name|AbbreviatedObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjectId
name|insId
range|:
name|inserted
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|id
operator|.
name|prefixCompare
argument_list|(
name|insId
argument_list|)
operator|==
literal|0
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|insId
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|addAll
argument_list|(
name|reader
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|open (AnyObjectId objectId, int typeHint)
specifier|public
name|ObjectLoader
name|open
parameter_list|(
name|AnyObjectId
name|objectId
parameter_list|,
name|int
name|typeHint
parameter_list|)
throws|throws
name|IOException
block|{
name|InsertedObject
name|obj
init|=
name|inserted
operator|.
name|get
argument_list|(
name|objectId
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
name|reader
operator|.
name|open
argument_list|(
name|objectId
argument_list|,
name|typeHint
argument_list|)
return|;
block|}
if|if
condition|(
name|typeHint
operator|!=
name|OBJ_ANY
operator|&&
name|obj
operator|.
name|type
argument_list|()
operator|!=
name|typeHint
condition|)
block|{
throw|throw
operator|new
name|IncorrectObjectTypeException
argument_list|(
name|objectId
operator|.
name|copy
argument_list|()
argument_list|,
name|typeHint
argument_list|)
throw|;
block|}
return|return
name|obj
operator|.
name|newLoader
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getShallowCommits ()
specifier|public
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|getShallowCommits
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|reader
operator|.
name|getShallowCommits
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do nothing; this class owns no open resources.
block|}
annotation|@
name|Override
DECL|method|getCreatedFromInserter ()
specifier|public
name|ObjectInserter
name|getCreatedFromInserter
parameter_list|()
block|{
return|return
name|InMemoryInserter
operator|.
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

