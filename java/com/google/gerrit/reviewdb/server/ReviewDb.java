begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
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
name|client
operator|.
name|Change
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
name|gwtorm
operator|.
name|server
operator|.
name|Relation
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
name|Schema
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
name|Sequence
import|;
end_import

begin_comment
comment|/**  * The review service database schema.  *  *<p>Root entities that are at the top level of some important data graph:  *  *<ul>  *<li>{@link Change}: All review information about a single proposed change.  *</ul>  */
end_comment

begin_interface
DECL|interface|ReviewDb
specifier|public
interface|interface
name|ReviewDb
extends|extends
name|Schema
block|{
comment|/* If you change anything, update ReviewDbSchemaVersion.C to use a new version. */
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|method|schemaVersion ()
name|SchemaVersionAccess
name|schemaVersion
parameter_list|()
function_decl|;
comment|// Deleted @Relation(id = 2)
comment|// Deleted @Relation(id = 3)
comment|// Deleted @Relation(id = 4)
comment|// Deleted @Relation(id = 6)
comment|// Deleted @Relation(id = 7)
comment|// Deleted @Relation(id = 8)
comment|// Deleted @Relation(id = 10)
comment|// Deleted @Relation(id = 11)
comment|// Deleted @Relation(id = 12)
comment|// Deleted @Relation(id = 13)
comment|// Deleted @Relation(id = 17)
comment|// Deleted @Relation(id = 18)
comment|// Deleted @Relation(id = 19)
comment|// Deleted @Relation(id = 20)
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|21
argument_list|)
DECL|method|changes ()
name|ChangeAccess
name|changes
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|22
argument_list|)
DECL|method|patchSetApprovals ()
name|PatchSetApprovalAccess
name|patchSetApprovals
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|23
argument_list|)
DECL|method|changeMessages ()
name|ChangeMessageAccess
name|changeMessages
parameter_list|()
function_decl|;
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|24
argument_list|)
DECL|method|patchSets ()
name|PatchSetAccess
name|patchSets
parameter_list|()
function_decl|;
comment|// Deleted @Relation(id = 25)
annotation|@
name|Relation
argument_list|(
name|id
operator|=
literal|26
argument_list|)
DECL|method|patchComments ()
name|PatchLineCommentAccess
name|patchComments
parameter_list|()
function_decl|;
comment|// Deleted @Relation(id = 28)
comment|// Deleted @Relation(id = 29)
comment|// Deleted @Relation(id = 30)
DECL|field|FIRST_ACCOUNT_ID
name|int
name|FIRST_ACCOUNT_ID
init|=
literal|1000000
decl_stmt|;
DECL|field|FIRST_GROUP_ID
name|int
name|FIRST_GROUP_ID
init|=
literal|1
decl_stmt|;
DECL|field|FIRST_CHANGE_ID
name|int
name|FIRST_CHANGE_ID
init|=
literal|1
decl_stmt|;
comment|/**    * Next unique id for a {@link Change}.    *    * @deprecated use {@link com.google.gerrit.server.Sequences#nextChangeId()}.    */
annotation|@
name|Sequence
argument_list|(
name|startWith
operator|=
name|FIRST_CHANGE_ID
argument_list|)
annotation|@
name|Deprecated
DECL|method|nextChangeId ()
name|int
name|nextChangeId
parameter_list|()
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

