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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Constants
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

begin_interface
DECL|interface|AdminConstants
specifier|public
interface|interface
name|AdminConstants
extends|extends
name|Constants
block|{
DECL|field|I
name|AdminConstants
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|AdminConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|defaultAccountName ()
name|String
name|defaultAccountName
parameter_list|()
function_decl|;
DECL|method|defaultAccountGroupName ()
name|String
name|defaultAccountGroupName
parameter_list|()
function_decl|;
DECL|method|defaultBranchName ()
name|String
name|defaultBranchName
parameter_list|()
function_decl|;
DECL|method|defaultTagName ()
name|String
name|defaultTagName
parameter_list|()
function_decl|;
DECL|method|defaultRevisionSpec ()
name|String
name|defaultRevisionSpec
parameter_list|()
function_decl|;
DECL|method|annotation ()
name|String
name|annotation
parameter_list|()
function_decl|;
DECL|method|buttonDeleteIncludedGroup ()
name|String
name|buttonDeleteIncludedGroup
parameter_list|()
function_decl|;
DECL|method|buttonAddIncludedGroup ()
name|String
name|buttonAddIncludedGroup
parameter_list|()
function_decl|;
DECL|method|buttonDeleteGroupMembers ()
name|String
name|buttonDeleteGroupMembers
parameter_list|()
function_decl|;
DECL|method|buttonAddGroupMember ()
name|String
name|buttonAddGroupMember
parameter_list|()
function_decl|;
DECL|method|buttonSaveDescription ()
name|String
name|buttonSaveDescription
parameter_list|()
function_decl|;
DECL|method|buttonRenameGroup ()
name|String
name|buttonRenameGroup
parameter_list|()
function_decl|;
DECL|method|buttonCreateGroup ()
name|String
name|buttonCreateGroup
parameter_list|()
function_decl|;
DECL|method|buttonCreateProject ()
name|String
name|buttonCreateProject
parameter_list|()
function_decl|;
DECL|method|buttonChangeGroupOwner ()
name|String
name|buttonChangeGroupOwner
parameter_list|()
function_decl|;
DECL|method|buttonChangeGroupType ()
name|String
name|buttonChangeGroupType
parameter_list|()
function_decl|;
DECL|method|buttonSelectGroup ()
name|String
name|buttonSelectGroup
parameter_list|()
function_decl|;
DECL|method|buttonSaveChanges ()
name|String
name|buttonSaveChanges
parameter_list|()
function_decl|;
DECL|method|checkBoxEmptyCommit ()
name|String
name|checkBoxEmptyCommit
parameter_list|()
function_decl|;
DECL|method|checkBoxPermissionsOnly ()
name|String
name|checkBoxPermissionsOnly
parameter_list|()
function_decl|;
DECL|method|useContentMerge ()
name|String
name|useContentMerge
parameter_list|()
function_decl|;
DECL|method|useContributorAgreements ()
name|String
name|useContributorAgreements
parameter_list|()
function_decl|;
DECL|method|useSignedOffBy ()
name|String
name|useSignedOffBy
parameter_list|()
function_decl|;
DECL|method|createNewChangeForAllNotInTarget ()
name|String
name|createNewChangeForAllNotInTarget
parameter_list|()
function_decl|;
DECL|method|enableSignedPush ()
name|String
name|enableSignedPush
parameter_list|()
function_decl|;
DECL|method|requireSignedPush ()
name|String
name|requireSignedPush
parameter_list|()
function_decl|;
DECL|method|requireChangeID ()
name|String
name|requireChangeID
parameter_list|()
function_decl|;
DECL|method|rejectImplicitMerges ()
name|String
name|rejectImplicitMerges
parameter_list|()
function_decl|;
DECL|method|privateByDefault ()
name|String
name|privateByDefault
parameter_list|()
function_decl|;
DECL|method|workInProgressByDefault ()
name|String
name|workInProgressByDefault
parameter_list|()
function_decl|;
DECL|method|enableReviewerByEmail ()
name|String
name|enableReviewerByEmail
parameter_list|()
function_decl|;
DECL|method|matchAuthorToCommitterDate ()
name|String
name|matchAuthorToCommitterDate
parameter_list|()
function_decl|;
DECL|method|headingMaxObjectSizeLimit ()
name|String
name|headingMaxObjectSizeLimit
parameter_list|()
function_decl|;
DECL|method|headingGroupOptions ()
name|String
name|headingGroupOptions
parameter_list|()
function_decl|;
DECL|method|isVisibleToAll ()
name|String
name|isVisibleToAll
parameter_list|()
function_decl|;
DECL|method|buttonSaveGroupOptions ()
name|String
name|buttonSaveGroupOptions
parameter_list|()
function_decl|;
DECL|method|suggestedGroupLabel ()
name|String
name|suggestedGroupLabel
parameter_list|()
function_decl|;
DECL|method|parentSuggestions ()
name|String
name|parentSuggestions
parameter_list|()
function_decl|;
DECL|method|buttonBrowseProjects ()
name|String
name|buttonBrowseProjects
parameter_list|()
function_decl|;
DECL|method|projects ()
name|String
name|projects
parameter_list|()
function_decl|;
DECL|method|projectRepoBrowser ()
name|String
name|projectRepoBrowser
parameter_list|()
function_decl|;
DECL|method|headingGroupUUID ()
name|String
name|headingGroupUUID
parameter_list|()
function_decl|;
DECL|method|headingOwner ()
name|String
name|headingOwner
parameter_list|()
function_decl|;
DECL|method|headingDescription ()
name|String
name|headingDescription
parameter_list|()
function_decl|;
DECL|method|headingProjectOptions ()
name|String
name|headingProjectOptions
parameter_list|()
function_decl|;
DECL|method|headingProjectCommands ()
name|String
name|headingProjectCommands
parameter_list|()
function_decl|;
DECL|method|headingCommands ()
name|String
name|headingCommands
parameter_list|()
function_decl|;
DECL|method|headingMembers ()
name|String
name|headingMembers
parameter_list|()
function_decl|;
DECL|method|headingIncludedGroups ()
name|String
name|headingIncludedGroups
parameter_list|()
function_decl|;
DECL|method|noMembersInfo ()
name|String
name|noMembersInfo
parameter_list|()
function_decl|;
DECL|method|headingExternalGroup ()
name|String
name|headingExternalGroup
parameter_list|()
function_decl|;
DECL|method|headingCreateGroup ()
name|String
name|headingCreateGroup
parameter_list|()
function_decl|;
DECL|method|headingParentProjectName ()
name|String
name|headingParentProjectName
parameter_list|()
function_decl|;
DECL|method|columnProjectName ()
name|String
name|columnProjectName
parameter_list|()
function_decl|;
DECL|method|headingAgreements ()
name|String
name|headingAgreements
parameter_list|()
function_decl|;
DECL|method|headingAuditLog ()
name|String
name|headingAuditLog
parameter_list|()
function_decl|;
DECL|method|headingProjectSubmitType ()
name|String
name|headingProjectSubmitType
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_INHERIT ()
name|String
name|projectSubmitType_INHERIT
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_FAST_FORWARD_ONLY ()
name|String
name|projectSubmitType_FAST_FORWARD_ONLY
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_MERGE_ALWAYS ()
name|String
name|projectSubmitType_MERGE_ALWAYS
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_MERGE_IF_NECESSARY ()
name|String
name|projectSubmitType_MERGE_IF_NECESSARY
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_REBASE_IF_NECESSARY ()
name|String
name|projectSubmitType_REBASE_IF_NECESSARY
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_REBASE_ALWAYS ()
name|String
name|projectSubmitType_REBASE_ALWAYS
parameter_list|()
function_decl|;
DECL|method|projectSubmitType_CHERRY_PICK ()
name|String
name|projectSubmitType_CHERRY_PICK
parameter_list|()
function_decl|;
DECL|method|headingProjectState ()
name|String
name|headingProjectState
parameter_list|()
function_decl|;
DECL|method|projectState_ACTIVE ()
name|String
name|projectState_ACTIVE
parameter_list|()
function_decl|;
DECL|method|projectState_READ_ONLY ()
name|String
name|projectState_READ_ONLY
parameter_list|()
function_decl|;
DECL|method|projectState_HIDDEN ()
name|String
name|projectState_HIDDEN
parameter_list|()
function_decl|;
DECL|method|columnMember ()
name|String
name|columnMember
parameter_list|()
function_decl|;
DECL|method|columnEmailAddress ()
name|String
name|columnEmailAddress
parameter_list|()
function_decl|;
DECL|method|columnGroupName ()
name|String
name|columnGroupName
parameter_list|()
function_decl|;
DECL|method|columnGroupDescription ()
name|String
name|columnGroupDescription
parameter_list|()
function_decl|;
DECL|method|columnGroupType ()
name|String
name|columnGroupType
parameter_list|()
function_decl|;
DECL|method|columnGroupNotifications ()
name|String
name|columnGroupNotifications
parameter_list|()
function_decl|;
DECL|method|columnGroupVisibleToAll ()
name|String
name|columnGroupVisibleToAll
parameter_list|()
function_decl|;
DECL|method|columnDate ()
name|String
name|columnDate
parameter_list|()
function_decl|;
DECL|method|columnType ()
name|String
name|columnType
parameter_list|()
function_decl|;
DECL|method|columnByUser ()
name|String
name|columnByUser
parameter_list|()
function_decl|;
DECL|method|typeAdded ()
name|String
name|typeAdded
parameter_list|()
function_decl|;
DECL|method|typeRemoved ()
name|String
name|typeRemoved
parameter_list|()
function_decl|;
DECL|method|columnBranchName ()
name|String
name|columnBranchName
parameter_list|()
function_decl|;
DECL|method|columnBranchRevision ()
name|String
name|columnBranchRevision
parameter_list|()
function_decl|;
DECL|method|columnTagName ()
name|String
name|columnTagName
parameter_list|()
function_decl|;
DECL|method|columnTagRevision ()
name|String
name|columnTagRevision
parameter_list|()
function_decl|;
DECL|method|columnTagAnnotation ()
name|String
name|columnTagAnnotation
parameter_list|()
function_decl|;
DECL|method|initialRevision ()
name|String
name|initialRevision
parameter_list|()
function_decl|;
DECL|method|revision ()
name|String
name|revision
parameter_list|()
function_decl|;
DECL|method|buttonAddBranch ()
name|String
name|buttonAddBranch
parameter_list|()
function_decl|;
DECL|method|buttonDeleteBranch ()
name|String
name|buttonDeleteBranch
parameter_list|()
function_decl|;
DECL|method|buttonAddTag ()
name|String
name|buttonAddTag
parameter_list|()
function_decl|;
DECL|method|buttonDeleteTag ()
name|String
name|buttonDeleteTag
parameter_list|()
function_decl|;
DECL|method|saveHeadButton ()
name|String
name|saveHeadButton
parameter_list|()
function_decl|;
DECL|method|cancelHeadButton ()
name|String
name|cancelHeadButton
parameter_list|()
function_decl|;
DECL|method|groupItemHelp ()
name|String
name|groupItemHelp
parameter_list|()
function_decl|;
DECL|method|groupListTitle ()
name|String
name|groupListTitle
parameter_list|()
function_decl|;
DECL|method|groupFilter ()
name|String
name|groupFilter
parameter_list|()
function_decl|;
DECL|method|createGroupTitle ()
name|String
name|createGroupTitle
parameter_list|()
function_decl|;
DECL|method|groupTabGeneral ()
name|String
name|groupTabGeneral
parameter_list|()
function_decl|;
DECL|method|groupTabMembers ()
name|String
name|groupTabMembers
parameter_list|()
function_decl|;
DECL|method|groupTabAuditLog ()
name|String
name|groupTabAuditLog
parameter_list|()
function_decl|;
DECL|method|projectListTitle ()
name|String
name|projectListTitle
parameter_list|()
function_decl|;
DECL|method|projectFilter ()
name|String
name|projectFilter
parameter_list|()
function_decl|;
DECL|method|createProjectTitle ()
name|String
name|createProjectTitle
parameter_list|()
function_decl|;
DECL|method|projectListQueryLink ()
name|String
name|projectListQueryLink
parameter_list|()
function_decl|;
DECL|method|plugins ()
name|String
name|plugins
parameter_list|()
function_decl|;
DECL|method|pluginEnabled ()
name|String
name|pluginEnabled
parameter_list|()
function_decl|;
DECL|method|pluginDisabled ()
name|String
name|pluginDisabled
parameter_list|()
function_decl|;
DECL|method|pluginSettingsToolTip ()
name|String
name|pluginSettingsToolTip
parameter_list|()
function_decl|;
DECL|method|columnPluginName ()
name|String
name|columnPluginName
parameter_list|()
function_decl|;
DECL|method|columnPluginSettings ()
name|String
name|columnPluginSettings
parameter_list|()
function_decl|;
DECL|method|columnPluginVersion ()
name|String
name|columnPluginVersion
parameter_list|()
function_decl|;
DECL|method|columnPluginStatus ()
name|String
name|columnPluginStatus
parameter_list|()
function_decl|;
DECL|method|noGroupSelected ()
name|String
name|noGroupSelected
parameter_list|()
function_decl|;
DECL|method|errorNoMatchingGroups ()
name|String
name|errorNoMatchingGroups
parameter_list|()
function_decl|;
DECL|method|errorNoGitRepository ()
name|String
name|errorNoGitRepository
parameter_list|()
function_decl|;
DECL|method|addPermission ()
name|String
name|addPermission
parameter_list|()
function_decl|;
DECL|method|permissionNames ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|permissionNames
parameter_list|()
function_decl|;
DECL|method|refErrorEmpty ()
name|String
name|refErrorEmpty
parameter_list|()
function_decl|;
DECL|method|refErrorBeginSlash ()
name|String
name|refErrorBeginSlash
parameter_list|()
function_decl|;
DECL|method|refErrorDoubleSlash ()
name|String
name|refErrorDoubleSlash
parameter_list|()
function_decl|;
DECL|method|refErrorNoSpace ()
name|String
name|refErrorNoSpace
parameter_list|()
function_decl|;
DECL|method|refErrorPrintable ()
name|String
name|refErrorPrintable
parameter_list|()
function_decl|;
DECL|method|errorsMustBeFixed ()
name|String
name|errorsMustBeFixed
parameter_list|()
function_decl|;
DECL|method|sectionTypeReference ()
name|String
name|sectionTypeReference
parameter_list|()
function_decl|;
DECL|method|sectionTypeSection ()
name|String
name|sectionTypeSection
parameter_list|()
function_decl|;
DECL|method|sectionNames ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|sectionNames
parameter_list|()
function_decl|;
DECL|method|pagedListPrev ()
name|String
name|pagedListPrev
parameter_list|()
function_decl|;
DECL|method|pagedListNext ()
name|String
name|pagedListNext
parameter_list|()
function_decl|;
DECL|method|buttonCreate ()
name|String
name|buttonCreate
parameter_list|()
function_decl|;
DECL|method|buttonCreateDescription ()
name|String
name|buttonCreateDescription
parameter_list|()
function_decl|;
DECL|method|buttonCreateChange ()
name|String
name|buttonCreateChange
parameter_list|()
function_decl|;
DECL|method|buttonCreateChangeDescription ()
name|String
name|buttonCreateChangeDescription
parameter_list|()
function_decl|;
DECL|method|buttonEditConfig ()
name|String
name|buttonEditConfig
parameter_list|()
function_decl|;
DECL|method|buttonEditConfigDescription ()
name|String
name|buttonEditConfigDescription
parameter_list|()
function_decl|;
DECL|method|editConfigMessage ()
name|String
name|editConfigMessage
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

