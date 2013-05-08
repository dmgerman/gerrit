begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|extensions
operator|.
name|restapi
operator|.
name|ChildCollection
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
name|IdString
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
name|ResourceNotFoundException
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
name|RestReadView
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
name|RestView
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
name|PatchSet
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
name|PatchListNotAvailableException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Provider
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

begin_class
DECL|class|Files
class|class
name|Files
implements|implements
name|ChildCollection
argument_list|<
name|RevisionResource
argument_list|,
name|FileResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|fileInfoJson
specifier|private
specifier|final
name|FileInfoJson
name|fileInfoJson
decl_stmt|;
DECL|field|revisions
specifier|private
specifier|final
name|Provider
argument_list|<
name|Revisions
argument_list|>
name|revisions
decl_stmt|;
annotation|@
name|Inject
DECL|method|Files (DynamicMap<RestView<FileResource>> views, FileInfoJson fileInfoJson, Provider<Revisions> revisions)
name|Files
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|FileInfoJson
name|fileInfoJson
parameter_list|,
name|Provider
argument_list|<
name|Revisions
argument_list|>
name|revisions
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|fileInfoJson
operator|=
name|fileInfoJson
expr_stmt|;
name|this
operator|.
name|revisions
operator|=
name|revisions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|RevisionResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|AuthException
block|{
return|return
operator|new
name|List
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (RevisionResource rev, IdString id)
specifier|public
name|FileResource
name|parse
parameter_list|(
name|RevisionResource
name|rev
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|AuthException
block|{
return|return
operator|new
name|FileResource
argument_list|(
name|rev
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|class|List
specifier|private
specifier|final
class|class
name|List
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--base"
argument_list|,
name|metaVar
operator|=
literal|"revision-id"
argument_list|)
DECL|field|base
name|String
name|base
decl_stmt|;
annotation|@
name|Override
DECL|method|apply (RevisionResource resource)
specifier|public
name|Object
name|apply
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|PatchListNotAvailableException
block|{
name|PatchSet
name|basePatchSet
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|RevisionResource
name|baseResource
init|=
name|revisions
operator|.
name|get
argument_list|()
operator|.
name|parse
argument_list|(
name|resource
operator|.
name|getChangeResource
argument_list|()
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|base
argument_list|)
argument_list|)
decl_stmt|;
name|basePatchSet
operator|=
name|baseResource
operator|.
name|getPatchSet
argument_list|()
expr_stmt|;
block|}
return|return
name|fileInfoJson
operator|.
name|toFileInfoMap
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
argument_list|,
name|basePatchSet
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

